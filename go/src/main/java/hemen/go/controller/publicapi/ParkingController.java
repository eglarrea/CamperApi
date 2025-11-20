package hemen.go.controller.publicapi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.request.FilterParkingRequest;
import hemen.go.dto.response.FilterParkingResponse;
import hemen.go.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/public/parking")
@Tag(name="Parking")
public class ParkingController {
	private static final Logger logger = LoggerFactory.getLogger(ParkingController.class);

    // Servicios necesarios para busqueda 
    private final ParkingService parkingService;
    private final MessageSource messageSource;
    
    public ParkingController(ParkingService parkingService, MessageSource messageSource) {
        this.parkingService = parkingService;
        this.messageSource = messageSource;
    }
    
    @PostMapping("/find")
    public ResponseEntity<FilterParkingResponse> buscarParking(@RequestBody FilterParkingRequest request) {
        FilterParkingResponse response = parkingService.findParkings(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/parkings")
    @Operation(
        summary = "Obtener todos los parkings",
        description = "Método para obtener todos los parkings dados de alta.",
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
        @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. Los datos enviados no cumplen validaciones"),
        @ApiResponse(responseCode = "409", description = "Conflicto. Ya existe un usuario con el mismo email"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor durante el registro")
    })
    public ResponseEntity<?> getParkings() {
    	try {    		
    		
    		return ResponseEntity.ok(parkingService.findAll());
    		
    	} catch (DataIntegrityViolationException ex) {
    		  String mensaje = messageSource.getMessage("error.existe.usuario", null, LocaleContextHolder.getLocale() );
    	    return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
    	} catch (IllegalArgumentException e) {
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
