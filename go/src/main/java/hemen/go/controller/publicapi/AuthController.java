package hemen.go.controller.publicapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.request.LoginRequest;
import hemen.go.dto.response.JwtResponse;
import hemen.go.service.AuthService;

@RestController
@RequestMapping("/api/public/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    	 try {
    	        String token = authService.authenticate(request.getEmail(), request.getPassword());
    	        return ResponseEntity.ok(new JwtResponse(token));
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