package hemen.go.service;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.util.Date;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import hemen.go.entity.Reserva;
import hemen.go.entity.Usuario;
import hemen.go.repository.ReservaRepository;
import hemen.go.repository.UsuarioRepository;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenReservaService {
	private final UsuarioRepository usuarioRepository;
	private final MessageSource messageSource;
	
	
	/**
     * Clave secreta utilizada para firmar y validar los tokens JWT.
     * Debe tener al menos 256 bits (32 caracteres si se usa ASCII).
     */
    private final Key SECRET_KEY;

    public TokenReservaService(UsuarioRepository usuarioRepository,  MessageSource messageSource) {
        // Cargar dotenv en local, ignorar si no existe (producción)
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // Primero intenta leer de .env, si no existe usa System.getenv
        String secret = dotenv.get("JWT_SECRET", System.getenv("JWT_SECRET"));

        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET no definido o demasiado corto (mínimo 32 caracteres)");
        }

        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
        
        this.usuarioRepository = usuarioRepository;
        this.messageSource = messageSource;
    }
    
    public String generarToken(Reserva reserva) {
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
    }
    
    
    public String generarTokenPuerta(Long userId, Long idReserva,Long idParking) {
        long ahora = System.currentTimeMillis();
        long validez = 60 * 60 * 1000; // 30 minutos en milisegundos

        Date issuedAt = new Date();
        Date expiration = new Date(System.currentTimeMillis() + validez);
        System.out.println("IssuedAt: " + issuedAt);
        System.out.println("Expiration: " + expiration);
        
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
    
    public byte[] generarQRBytes(String token) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(token, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
