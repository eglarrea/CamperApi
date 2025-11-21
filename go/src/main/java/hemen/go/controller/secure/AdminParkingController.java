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
}
