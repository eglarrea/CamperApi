package hemen.go.controller.publicapi;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.request.LoginRequest;
import hemen.go.dto.response.JwtResponse;
import hemen.go.dto.response.UserDtoResponse;
import hemen.go.service.AuthService;
import hemen.go.service.UserService;

@RestController
@RequestMapping("/api/public/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    	 try {
    	        String token = authService.authenticate(request.getEmail(), request.getPassword());
    	        return ResponseEntity.ok(new JwtResponse(token));
    	    } catch (BadCredentialsException e) {
    	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    	                             .body("Credenciales inválidas");
    	    }
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.authenticate(request.getEmail(), request.getPassword());
            
            // Recuperar datos del usuario
            UserDtoResponse user = userService.findByEmail(request.getEmail());

            // Retornar token + datos del usuario
            return ResponseEntity.ok(new JwtResponse(token, user));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Credenciales inválidas");
        }
    }
  /*  @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }*/
}