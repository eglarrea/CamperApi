package hemen.go.controller.publicapi;

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
	
    // Servicios necesarios para busqueda 
    private final ParkingService parkingService;
    
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
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
        @ApiResponse(responseCode = "200", description = "Retorna la lista de parkins"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> getParkings() {
    		return ResponseEntity.ok(parkingService.findAll());
    }
}
