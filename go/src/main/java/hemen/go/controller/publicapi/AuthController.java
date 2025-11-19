package hemen.go.controller.publicapi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import hemen.go.dto.request.LoginRequest;
import hemen.go.dto.request.RegisterRequest;
import hemen.go.dto.response.JwtResponse;
import hemen.go.dto.response.UserDtoResponse;
import hemen.go.service.AuthService;
import hemen.go.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
@Tag(name="Autenticación y Registro")
public class AuthController {

    // Logger para registrar eventos y errores
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    // Servicios necesarios para autenticación y gestión de usuarios
    private final AuthService authService;
    private final UserService userService;
    private LocaleResolver localeResolver;

    // Fuente de mensajes para internacionalización (i18n)
    private final MessageSource messageSource;
    
    /**
     * Constructor con inyección de dependencias.
     *
     * @param authService servicio de autenticación (login, registro, generación de JWT).
     * @param userService servicio de gestión de usuarios (consultas, datos).
     * @param messageSource fuente de mensajes para internacionalización.
     */
    public AuthController(AuthService authService, UserService userService, MessageSource messageSource,LocaleResolver localeResolver) {
        this.authService = authService;
        this.userService = userService;
        this.messageSource = messageSource;
        this.localeResolver=localeResolver;
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
    @Operation(
        summary = "Loguearse en la API",
        description = "Permite a un usuario autenticarse con su email y contraseña. " +
                      "Si las credenciales son válidas, devuelve un token JWT junto con los datos del usuario."
    )
    @ApiResponses(value = {
        @ApiResponse (responseCode = "200", description = "Autenticación exitosa. Devuelve token y datos del usuario"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. El cuerpo de la petición no cumple el formato esperado"),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas. No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor durante la autenticación")
    })
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
            String mensaje = messageSource.getMessage("auth.invalid.credentials", null, LocaleContextHolder.getLocale());
            logger.error("Locale:" + LocaleContextHolder.getLocale());
            logger.error("mensaje:" +mensaje);
            logger.error("LocaleResolver en uso: " + localeResolver.getClass().getName());
            // Respuesta con estado 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensaje);
        }
    }

    
    
    @PostMapping("/register")
    @Operation(
        summary = "Registrarse en la aplicación",
        description = "Permite a un nuevo usuario registrarse en la aplicación enviando sus datos personales. "
                    + "Si los datos son válidos y no existe un usuario con el mismo email, se crea el registro."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. Los datos enviados no cumplen validaciones"),
        @ApiResponse(responseCode = "409", description = "Conflicto. Ya existe un usuario con el mismo email"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor durante el registro")
    })
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult result) {
    	try {
    		if (result.hasErrors()) {
    	        List<String> errores = result.getAllErrors().stream()
    	            .map(ObjectError::getDefaultMessage) // aquí ya viene interpolado
    	            .toList();
    	        return ResponseEntity.badRequest().body(errores);
    	    }
    		
    		authService.register(request);
    		return ResponseEntity.ok(messageSource.getMessage("message.ok.usuario.creado", null, LocaleContextHolder.getLocale()));
    	} catch (DataIntegrityViolationException ex) {
    		  String mensaje = messageSource.getMessage("error.existe.usuario", null, LocaleContextHolder.getLocale() );
    	    return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
    	} catch (IllegalArgumentException e) {
                // Log de error con credenciales inválidas
                logger.error("Datos no validos: {}", e.getMessage());  
                return ResponseEntity.badRequest().body(e.getMessage());
    	} catch (jakarta.validation.ConstraintViolationException e) {
    		List<String> errores = e.getConstraintViolations().stream()
    		    .map(v -> "Campo '" + v.getPropertyPath() + "' " + v.getMessage() + 
    		            " (valor: " + v.getInvalidValue() + ")").toList();

    		return ResponseEntity.badRequest().body(errores);
        }
    }
    
    
    
    
}
