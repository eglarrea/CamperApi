package hemen.go.controller.secure;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.request.RegisterRequest;
import hemen.go.dto.response.UserDtoResponse;
import hemen.go.entity.Usuario;
import hemen.go.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/api/user")
public class UserController {

    //private final UsuarioRepository usuarioRepository;
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
        @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token válido"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado en la base de datos")
    })
    public UserDtoResponse updateMyData(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @RequestBody RegisterRequest updatedData) {
    	return userService.updateUserData(userDetails.getUsername(), updatedData);
        /*Usuario usuario = usuarioRepository.findByEmailPersona(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Actualizar solo si el campo no es nulo y ha cambiado
        if (updatedData.getNombrePersona() != null 
                && !updatedData.getNombrePersona().equals(usuario.getNombre_persona())) {
            usuario.setNombre_persona(updatedData.getNombrePersona());
        }

        if (updatedData.getEmailPersona() != null 
                && !updatedData.getEmailPersona().equals(usuario.getEmailPersona())) {
            usuario.setEmailPersona(updatedData.getEmailPersona());
        }

        // Validar y actualizar contraseña solo si se ha enviado y coincide con confirmación
        if (updatedData.getPassPersona() != null && !updatedData.getPassPersona().isBlank()) {
            if (!updatedData.getPassPersona().equals(updatedData.getConfirmPassPersona())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La confirmación de la contraseña no coincide");
            }
            String encodedPassword = passwordEncoder.encode(updatedData.getPassword());
            usuario.setPassword(encodedPassword);
        }

        Usuario usuario2 = usuarioRepository.save(usuario);

        return new UserDtoResponse(
                usuario2.getId(),
                usuario2.getNombre_persona(),
                usuario2.getApellidos_persona(),
                usuario2.getFec_nacimiento_persona(),
                usuario2.getDni_persona(),
                usuario2.getIban_persona(),
                usuario2.getEmailPersona(),
                usuario2.is_admin(),
                usuario2.getEmpresa() != null ? usuario2.getEmpresa().getNombreEmpresa() : null
        );*/
    }
}
