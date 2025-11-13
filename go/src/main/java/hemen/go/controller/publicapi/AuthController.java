package hemen.go.controller.publicapi;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/public/auth")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final UserService userService;
    private final MessageSource messageSource;

    public AuthController(AuthService authService, UserService userService, MessageSource messageSource) {
        this.authService = authService;
        this.userService = userService;
        this.messageSource = messageSource;
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
        	logger.error("Credenciales no válidas para email: {} y password: {}", request.getEmail(), request.getPassword());
        	String mensaje = messageSource.getMessage(
                    "auth.invalid.credentials",   // clave en messages.properties
                    null,                         // parámetros opcionales
                    LocaleContextHolder.getLocale() // detecta el idioma del cliente
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensaje);
        }
    }
  /*  @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
	     if (!request.getPassPersona().equals(request.getConfirmPassPersona())) {
	        throw new IllegalArgumentException("Las contraseñas no coinciden");
	    }
        authService.register(request);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }*/
}