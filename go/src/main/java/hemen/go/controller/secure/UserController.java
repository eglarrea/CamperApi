package hemen.go.controller.secure;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.request.RegisterRequest;
import hemen.go.entity.Usuario;
import hemen.go.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/user")
public class UserController {

    // Logger para registrar eventos y errores
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    //private final UsuarioRepository usuarioRepository;
    private final UserService userService;
    
    // Fuente de mensajes para internacionalización (i18n)
    private final MessageSource messageSource;

    public UserController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    /**
     * Endpoint para que el ADMIN consulte los datos de cualquier usuario por id.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Este endpoint permite a un administrador obtener los datos de un usuario específico mediante su ID.",
   		security = { @SecurityRequirement (name = "bearerAuth") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado. Solo administradores pueden usar este endpoint"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID proporcionado")
    })
   
    public Usuario getUserById(@PathVariable Long id) {
    	return userService.getUserById(id);
    }

    /**
     * Endpoint para que el usuario autenticado consulte sus propios datos.
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
        summary = "Obtener datos del usuario autenticado",
        description = "Devuelve la información del usuario actualmente autenticado en el sistema. " +
                      "Disponible para roles USER y ADMIN.",
        security = { @SecurityRequirement (name = "bearerAuth") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Datos del usuario obtenidos correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado en la base de datos")
    })
    public Usuario getMyData(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
    	return userService.getMyData(userDetails.getUsername());
        /*return usuarioRepository.findByEmailPersona(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));*/
    }

    /**
     * Endpoint para actualizar los datos del usuario autenticado.
     */
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
        summary = "Actualizar datos del usuario autenticado",
        description = "Permite a un usuario autenticado (roles USER o ADMIN) actualizar sus datos personales. "
                    + "El cuerpo de la petición debe contener los campos a modificar."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Datos del usuario actualizados correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. Los datos enviados no cumplen validaciones"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado en la base de datos")
    })
    public ResponseEntity<?> updateMyData(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @Valid @RequestBody RegisterRequest updatedData , BindingResult result) {
    	
    	try {
    		if (result.hasErrors()) {
    	        List<String> errores = result.getAllErrors().stream()
    	            .map(ObjectError::getDefaultMessage) // aquí ya viene interpolado
    	            .toList();
    	        return ResponseEntity.badRequest().body(errores);
    	    }
    		
    		userService.updateUserData(userDetails.getUsername(), updatedData);
    		return ResponseEntity.ok(messageSource.getMessage("message.ok.usuario.actualizado", null, LocaleContextHolder.getLocale()));
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
