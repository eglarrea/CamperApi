package hemen.go.controller.publicapi;

import java.util.List;
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
import hemen.go.dto.request.RegisterRequest;
import hemen.go.dto.response.JwtResponse;
import hemen.go.dto.response.UserDtoResponse;
import hemen.go.service.AuthService;
import hemen.go.service.UserService;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador REST público para la autenticación de usuarios.
 *
 * Endpoints expuestos bajo la ruta "/api/public/auth".
 * 
 * Funcionalidades principales:
 *  - Login de usuarios con email y contraseña.
 *  - Retorno de un token JWT junto con los datos del usuario autenticado.
 *  - Manejo de errores de credenciales inválidas con mensajes internacionalizados.
 */
@RestController
@RequestMapping("/api/public/auth")
public class AuthController {

    // Logger para registrar eventos y errores
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    // Servicios necesarios para autenticación y gestión de usuarios
    private final AuthService authService;
    private final UserService userService;

    // Fuente de mensajes para internacionalización (i18n)
    private final MessageSource messageSource;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param authService servicio de autenticación (login, registro, generación de JWT).
     * @param userService servicio de gestión de usuarios (consultas, datos).
     * @param messageSource fuente de mensajes para internacionalización.
     */
    public AuthController(AuthService authService, UserService userService, MessageSource messageSource) {
        this.authService = authService;
        this.userService = userService;
        this.messageSource = messageSource;
    }

    /**
     * Endpoint POST para login de usuarios.
     *
     * Flujo:
     *  1. Recibe un objeto LoginRequest con email y contraseña.
     *  2. Llama a AuthService.authenticate() para validar credenciales y generar token JWT.
     *  3. Recupera los datos del usuario con UserService.findByEmail().
     *  4. Devuelve un objeto JwtResponse con el token y los datos del usuario.
     *
     * Manejo de errores:
     *  - Si las credenciales son inválidas, se captura BadCredentialsException.
     *  - Se registra el error en el log.
     *  - Se devuelve un mensaje internacionalizado (auth.invalid.credentials) con estado HTTP 401.
     *
     * @param request objeto con email y contraseña.
     * @return ResponseEntity con JwtResponse si éxito, o mensaje de error si fallo.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Autenticación y generación de token
            String token = authService.authenticate(request.getEmail(), request.getPassword());

            // Recuperar datos del usuario
            UserDtoResponse user = userService.findByEmail(request.getEmail());

            // Retornar token + datos del usuario
            return ResponseEntity.ok(new JwtResponse(token, user));
        } catch (BadCredentialsException e) {
            // Log de error con credenciales inválidas
            logger.error("Credenciales no válidas para email: {} y password: {}", request.getEmail(), request.getPassword());

            // Mensaje internacionalizado según el idioma del cliente
            String mensaje = messageSource.getMessage(
                "auth.invalid.credentials",   // clave definida en messages.properties
                null,                         // parámetros opcionales
                LocaleContextHolder.getLocale() // detecta el idioma del cliente
            );

            // Respuesta con estado 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensaje);
        }
    }

    
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    	try {
    		authService.register(request);
    		return ResponseEntity.ok("Usuario registrado correctamente");
    	} catch (ConstraintViolationException e) {
    		List<String> errores = e.getConstraintViolations().stream()
    		    .map(v -> "Campo '" + v.getPropertyPath() + "' " + v.getMessage() + 
    		            " (valor: " + v.getInvalidValue() + ")").toList();

    		return ResponseEntity.badRequest().body(errores);
        }
    }
    
}
