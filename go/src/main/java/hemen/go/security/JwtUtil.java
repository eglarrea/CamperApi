package hemen.go.security;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

/**
 * Utilidad para la gestión de tokens JWT en la aplicación Hemengo.
 *
 * Funcionalidades principales:
 *  - Generar tokens JWT firmados con una clave secreta segura.
 *  - Extraer el nombre de usuario (subject) de un token.
 *  - Validar tokens comparando el usuario y la fecha de expiración.
 *  - Comprobar si un token ha expirado.
 *
 * Uso:
 *  Esta clase se utiliza junto con JwtFilter y Spring Security para
 *  autenticar peticiones basadas en JWT.
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta utilizada para firmar y validar los tokens JWT.
     * Debe tener al menos 256 bits (32 caracteres si se usa ASCII).
     */
    private final Key SECRET_KEY;

    public JwtUtil() {
        // Cargar dotenv en local, ignorar si no existe (producción)
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // Primero intenta leer de .env, si no existe usa System.getenv
        String secret = dotenv.get("JWT_SECRET", System.getenv("JWT_SECRET"));

        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET no definido o demasiado corto (mínimo 32 caracteres)");
        }

        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Genera un token JWT para un usuario.
     *
     * - El subject se establece como el nombre de usuario.
     * - Se añade la fecha de emisión (issuedAt).
     * - Se establece una expiración de 1 hora.
     * - Se firma con la clave secreta.
     *
     * @param userDetails detalles del usuario autenticado.
     * @return token JWT en formato String.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())              // nombre de usuario
                .setIssuedAt(new Date(System.currentTimeMillis()))  // fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // expira en 1 hora
                .signWith(SECRET_KEY)                               // firma con clave secreta
                .compact();
    }

    /**
     * Extrae el nombre de usuario (subject) de un token JWT.
     *
     * @param token el JWT recibido.
     * @return el nombre de usuario contenido en el token.
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valida un token JWT.
     *
     * - Comprueba que el nombre de usuario en el token coincide con el de UserDetails.
     * - Verifica que el token no esté expirado.
     *
     * @param token el JWT recibido.
     * @param userDetails detalles del usuario autenticado.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && 
               !isTokenExpired(token);
    }

    /**
     * Comprueba si un token JWT ha expirado.
     *
     * @param token el JWT recibido.
     * @return true si el token está expirado, false si aún es válido.
     */
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
    
    public Jws<Claims> validarTokenPuerta(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
    }
}
