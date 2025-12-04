package hemen.go.controller.publicapi;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.request.FilterParkingRequest;
import hemen.go.dto.response.ParkingDtoFindResponse;
import hemen.go.dto.response.ParkingDtoResponse;
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
	
    // Servicios necesarios para busqueda 
    private final ParkingService parkingService;
    private final MessageSource messageSource;
    public ParkingController(ParkingService parkingService, MessageSource messageSource) {
        this.parkingService = parkingService;
        this.messageSource = messageSource;
    }
    
    @PostMapping("/find")
    public ResponseEntity<?>  buscarParking(@RequestBody FilterParkingRequest request) {
        List<ParkingDtoFindResponse> response = parkingService.findParkings(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
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
        @ApiResponse(responseCode = "200", description = "Retorna la lista de parkins"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> getParkings(@PathVariable Long id) {
    	
    	 try {
         	//TODO VER QUE DATOS NECESITAMOS PARA HACER EL REPONSE 
    		 ParkingDtoResponse parking = parkingService.findById(id);
            // ReservaDtoResponse response = new ReservaDtoResponse(reserva);
             return ResponseEntity.ok(parking);
         } catch (NoSuchElementException e) {
             String mensaje = messageSource.getMessage("error.parking.no.existe", null, LocaleContextHolder.getLocale());
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
         }
    }
    
}
