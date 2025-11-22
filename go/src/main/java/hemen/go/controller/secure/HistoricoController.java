package hemen.go.controller.secure;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.response.ReservaResponse;
import hemen.go.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/historico")
@Tag(name="Reservas Historicas")
public class HistoricoController {
	
	 // Logger para registrar eventos y errores
    private static final Logger logger = LoggerFactory.getLogger(HistoricoController.class);
    
    private final ReservaService reservaService;
    

    public HistoricoController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }
    
    @GetMapping("/listado")
    @Operation(
        summary = "Obtener listado de historico de reservas",
        description = "Devuelve el listado de historico de reservas",
        security = { @SecurityRequirement(name = "bearerAuth") },
		parameters = {                    		  
	              @Parameter(
	            		   name = "Accept-Language",
	                       description = "Idioma de la respuesta (es, en, eu)",
	                       in = ParameterIn.HEADER,
	                       required = false
	            		  )
	        }
    )
    @PreAuthorize("hasAnyRole('USER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle de la reserva devuelto correctamente"),
    })
    public ResponseEntity<?> getListHistoricoReservas(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
    	
    	List<ReservaResponse> historico = reservaService.getHistoricoReservas(userDetails.getUsername());
    	logger.info("Listado reservas historicas:"+userDetails.getUsername()+" "+historico.toString());
    	return ResponseEntity.ok(historico); 
    	
    }
}
