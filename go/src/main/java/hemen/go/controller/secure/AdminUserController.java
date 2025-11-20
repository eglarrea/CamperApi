package hemen.go.controller.secure;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.response.UserDtoResponse;
import hemen.go.entity.Usuario;
import hemen.go.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/users")
@Tag(name="Usuarios (Administrador)")
public class AdminUserController {
	
	private final UserService userService;

    public AdminUserController(UserService userService) {
	        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Obtener la lista de usuarios",
            description = "Devuelve la lista de usuarios de la empresa a la que pertenece el administrador. " +
                          "Disponible  ADMIN.",
            security = { @SecurityRequirement (name = "bearerAuth") },
    		parameters = {                    		  
                @Parameter(
              		   name = "Accept-Language",
                         description = "Idioma de la respuesta (es, en, eu)",
                         in = ParameterIn.HEADER,
                         required = false
              		  )
              }
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos del usuario obtenidos correctamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
            @ApiResponse(responseCode = "404", description = "Usuario erroneo")
        })
    public ResponseEntity<?> getUsersByCompany(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
    	 try {
        
    		 // Buscar todos los usuarios de esa empresa
	        List<UserDtoResponse> users = userService.findByCompanyId(userDetails.getUsername());

	        return ResponseEntity.ok(users);
    	 } catch (UsernameNotFoundException e) {
             
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
         }
    }
    
    /**
     * Endpoint para que el ADMIN consulte los datos de cualquier usuario por id.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Este endpoint permite a un administrador obtener los datos de un usuario específico mediante su ID.",
   		security = { @SecurityRequirement (name = "bearerAuth") },
		parameters = {                    		  
            @Parameter(
          		   name = "Accept-Language",
                     description = "Idioma de la respuesta (es, en, eu)",
                     in = ParameterIn.HEADER,
                     required = false
          		  )
          }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado. Solo administradores pueden usar este endpoint"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID proporcionado")
    })
   
    public Usuario getUserById(@PathVariable Long id) {
    	return userService.getUserById(id);
    }
}
