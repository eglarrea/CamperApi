package hemen.go.controller.secure;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import hemen.go.dto.request.RegisterRequest;
import hemen.go.dto.response.UserDtoResponse;
import hemen.go.entity.Usuario;
import hemen.go.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UsuarioRepository usuarioRepository;

    public UserController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Endpoint para que el ADMIN consulte los datos de cualquier usuario por id.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Este endpoint permite a un administrador obtener los datos de un usuario específico mediante su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado. Solo administradores pueden usar este endpoint"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID proporcionado")
    })
    public Usuario getUserById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    /**
     * Endpoint para que el usuario autenticado consulte sus propios datos.
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
        summary = "Obtener datos del usuario autenticado",
        description = "Devuelve la información del usuario actualmente autenticado en el sistema. " +
                      "Disponible para roles USER y ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Datos del usuario obtenidos correctamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token válido"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado en la base de datos")
    })
    public Usuario getMyData(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        return usuarioRepository.findByEmailPersona(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
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
    public UserDtoResponse updateMyData(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
                                @RequestBody RegisterRequest updatedData) {
    	Usuario usuario = usuarioRepository.findByEmailPersona(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre_persona(updatedData.getNombrePersona());
        usuario.setEmailPersona(updatedData.getEmailPersona());
        // añade aquí los campos que quieras actualizar
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
        );
      // return usuarioRepository.save(usuario);
    }
}
