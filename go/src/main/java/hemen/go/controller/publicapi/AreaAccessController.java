package hemen.go.controller.publicapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.request.TokenRequest;
import hemen.go.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;


@RestController
@RequestMapping("/api/public")
public class AreaAccessController {

	private final JwtUtil jwtUtil;
    
    public AreaAccessController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
       
    }
    
    @PostMapping("/acceder")
    public ResponseEntity<?> abrirPuerta(@RequestBody TokenRequest request) {
        try {
            Jws<Claims> claims = jwtUtil.validarTokenPuerta(request.getToken());

            Long idParking = claims.getBody().get("idParking", Long.class);
            if(idParking == request.getIdParking())
            	return ResponseEntity.ok("Puerta abierta correctamente");
            else
            	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Qr no valido");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token caducado");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }
}
