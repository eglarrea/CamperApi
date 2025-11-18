package hemen.go.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Servicio encargado de la generación de tokens JWT asociados a reservas y accesos,
 * así como la creación de códigos QR que contienen dichos tokens.
 * 
 * <p>Este servicio se utiliza para:
 * <ul>
 *   <li>Generar un token JWT temporal para apertura de puertas.</li>
 *   <li>Generar un código QR en formato PNG a partir de un token.</li>
 * </ul>
 * 
 * <p>El secreto JWT se obtiene de variables de entorno (.env o System.getenv),
 * y debe tener al menos 32 caracteres para garantizar seguridad.</p>
 * 
 * @author 
 */
@Service
public class TokenReservaService {
    
    /** Clave secreta utilizada para firmar los JWT. */
    private final Key SECRET_KEY;

    /**
     * Constructor del servicio. Inicializa la clave secreta JWT a partir de
     * variables de entorno y valida su longitud mínima.
     *
     * @param usuarioRepository repositorio de usuarios
     * @param messageSource fuente de mensajes para i18n
     * @throws IllegalStateException si la clave JWT no está definida o es demasiado corta
     */
    public TokenReservaService() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String secret = dotenv.get("JWT_SECRET", System.getenv("JWT_SECRET"));

        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET no definido o demasiado corto (mínimo 32 caracteres)");
        }

        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    /**
     * Genera un token JWT asociado a una reserva.
     * 
     * <p>El token incluye información como:
     * <ul>
     *   <li>ID de la persona</li>
     *   <li>ID del parking</li>
     *   <li>ID de la plaza</li>
     *   <liFechas de inicio, fin y alta de la reserva</li>
     * </ul>
     * </p>
     *
     * @param reserva objeto {@link Reserva} con los datos de la reserva
     * @return token JWT firmado
     */
    /*public String generarToken(Reserva reserva) {
        return Jwts.builder()
                .setSubject("reserva-" + reserva.getId())
                .claim("idPersona", reserva.getPersona().getId())
                .claim("idParking", reserva.getPlaza().getParking().getId())
                .claim("idPlaza", reserva.getPlaza().getId())
                .claim("fecInicio", reserva.getFecInicio())
                .claim("fecFin", reserva.getFecFin())
                .claim("fecAlta", reserva.getFecAlta())
                .setIssuedAt(reserva.getFecInicio())
                .setExpiration(reserva.getFecFin())
                .signWith(SECRET_KEY)
                .compact();
    }*/
    
    /**
     * Genera un token JWT temporal para apertura de puertas.
     * 
     * <p>El token tiene una validez de 1 hora e incluye:
     * <ul>
     *   <li>ID del usuario</li>
     *   <li>ID de la reserva</li>
     *   <li>ID del parking</li>
     * </ul>
     * </p>
     *
     * @param userId identificador del usuario
     * @param idReserva identificador de la reserva
     * @param idParking identificador del parking
     * @return token JWT firmado válido por 1 hora
     */
    public String generarTokenPuerta(Long userId, Long idReserva, Long idParking) {
        long validez = 60 * 60 * 1000; // 1 hora en milisegundos
        Date issuedAt = new Date();
        Date expiration = new Date(System.currentTimeMillis() + validez);
        
        return Jwts.builder()
                .setSubject("abrir-puerta")
                .claim("idUsuario", userId)
                .claim("idReserva", idReserva)
                .claim("idParking", idParking)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }
    
    /**
     * Genera un código QR en formato PNG a partir de un token JWT.
     * 
     * <p>El QR se genera con tamaño 300x300 píxeles.</p>
     *
     * @param token cadena JWT a codificar en el QR
     * @return arreglo de bytes que representa la imagen PNG del QR
     * @throws WriterException si ocurre un error al generar el QR
     * @throws IOException si ocurre un error al escribir la imagen
     */
    public byte[] generarQRBytes(String token) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(token, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
