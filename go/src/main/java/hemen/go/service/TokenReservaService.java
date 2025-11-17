package hemen.go.service;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.security.Key;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import hemen.go.entity.Reserva;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenReservaService {

	
	/**
     * Clave secreta utilizada para firmar y validar los tokens JWT.
     * Debe tener al menos 256 bits (32 caracteres si se usa ASCII).
     */
    private final Key SECRET_KEY;

    public TokenReservaService() {
        // Cargar dotenv en local, ignorar si no existe (producción)
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // Primero intenta leer de .env, si no existe usa System.getenv
        String secret = dotenv.get("JWT_SECRET", System.getenv("JWT_SECRET"));

        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET no definido o demasiado corto (mínimo 32 caracteres)");
        }

        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
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
    
    
    public byte[] generarQRBytes(String token) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(token, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
