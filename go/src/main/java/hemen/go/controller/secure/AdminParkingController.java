package hemen.go.controller.secure;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.request.PlazaRequest;
import hemen.go.dto.response.ParkingDtoResponse;
import hemen.go.dto.response.PlazaResponse;
import hemen.go.service.ParkingService;
import hemen.go.service.PlazaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/parking")
@Tag(name = "Parkings (Administrador)")
public class AdminParkingController {

	private ParkingService parkingService;
	private PlazaService plazaService;
	
	public AdminParkingController(ParkingService parkingService,PlazaService plazaService) {
		this.parkingService = parkingService;
		this.plazaService = plazaService;
	}
	

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Obtener la lista de parkings", description = "Devuelve la lista de parking de la empresa a la que pertenece el administrador. "
			+ "Disponible  ADMIN.", security = { @SecurityRequirement(name = "bearerAuth") }, parameters = {
					@Parameter(name = "Accept-Language", description = "Idioma de la respuesta (es, en, eu)", in = ParameterIn.HEADER, required = false) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Datos parkings obtenidos correctamente"),
			@ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
			@ApiResponse(responseCode = "404", description = "Usuario erroneo") })
	public ResponseEntity<?> getParkingsByCompany(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
		try {

			// Busca los parkings de por empresa
			List<ParkingDtoResponse> users = parkingService.findByCompanyId(userDetails.getUsername());

			return ResponseEntity.ok(users);
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	
	@GetMapping("/plaza/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Obtener los datos de una plaza", description = "Devuelve los datos de una plazadel parking de la empresa a la que pertenece el administrador. "
			+ "Disponible  ADMIN.", security = { @SecurityRequirement(name = "bearerAuth") }, parameters = {
					@Parameter(name = "Accept-Language", description = "Idioma de la respuesta (es, en, eu)", in = ParameterIn.HEADER, required = false) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Datos parkings obtenidos correctamente"),
			@ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
			@ApiResponse(responseCode = "404", description = "Usuario erroneo") })
	public ResponseEntity<?> getPlazaParkingsByCompany(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,@PathVariable Long id) {
		try {

			// obtiene los datos de una plaza
			PlazaResponse plaza = plazaService.findByCompanyAndPlazaId(userDetails.getUsername(), id);

			return ResponseEntity.ok(plaza);
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	
	@PostMapping("/{parkingId}/plazas")
    @PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Da de alta una plaza", description = "Dar de alta una nueva plaza al parking "
			+ "Disponible  ADMIN.", security = { @SecurityRequirement(name = "bearerAuth") }, parameters = {
					@Parameter(name = "Accept-Language", description = "Idioma de la respuesta (es, en, eu)", in = ParameterIn.HEADER, required = false) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Plaza dada de alta"),
			@ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
			@ApiResponse(responseCode = "404", description = "Usuario erroneo") })
    public ResponseEntity<PlazaResponse> addPlaza(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @PathVariable Long parkingId,
            @RequestBody PlazaRequest request
          ) {

   
        PlazaResponse response = plazaService.addPlazaToParking(userDetails.getUsername(), parkingId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualizar una plaza existente dentro de un parking
     */
    @PutMapping("/{parkingId}/plazas/{plazaId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar los datos de unaplaza", description = "Actualizar los datos de unaplaza"
			+ "Disponible  ADMIN.", security = { @SecurityRequirement(name = "bearerAuth") }, parameters = {
					@Parameter(name = "Accept-Language", description = "Idioma de la respuesta (es, en, eu)", in = ParameterIn.HEADER, required = false) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Plaza actualizada"),
			@ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
			@ApiResponse(responseCode = "404", description = "Usuario erroneo") })
    public ResponseEntity<PlazaResponse> updatePlaza(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @PathVariable Long parkingId,
            @PathVariable Long plazaId,
            @RequestBody PlazaRequest request
            ) {

        PlazaResponse response = plazaService.updatePlaza(userDetails.getUsername(), parkingId, plazaId, request);
        return ResponseEntity.ok(response);
    }
}
