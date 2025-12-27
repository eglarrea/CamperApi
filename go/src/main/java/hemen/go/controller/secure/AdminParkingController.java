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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.request.ParkingRequest;
import hemen.go.dto.request.ParkingUpdateRequest;
import hemen.go.dto.request.PlazaRequest;
import hemen.go.dto.request.validate.OnUpdate;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/parking")
@Tag(name = "Parkings (Administrador)")
public class AdminParkingController {

    private static final Logger logger = LoggerFactory.getLogger(AdminParkingController.class);
	private ParkingService parkingService;
	private PlazaService plazaService;
    private final MessageSource messageSource;
	
	public AdminParkingController(ParkingService parkingService,PlazaService plazaService,MessageSource messageSource) {
		this.parkingService = parkingService;
		this.plazaService = plazaService;
		this.messageSource = messageSource;
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
			List<ParkingDtoResponse> parking = parkingService.findByCompanyId(userDetails.getUsername());

			return ResponseEntity.ok(parking);
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Obtener datos de un parking", description = "Devuelve los datos del parking de la empresa a la que pertenece el administrador. "
			+ "Disponible  ADMIN.", security = { @SecurityRequirement(name = "bearerAuth") }, parameters = {
					@Parameter(name = "Accept-Language", description = "Idioma de la respuesta (es, en, eu)", in = ParameterIn.HEADER, required = false) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Datos parkings obtenidos correctamente"),
			@ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
			@ApiResponse(responseCode = "404", description = "Usuario erroneo") })
	public ResponseEntity<?> getParkingsByCompanyById(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails, @PathVariable Long id) {
		try {

			// Busca los parkings de por empresa
			ParkingDtoResponse parking = parkingService.findByCompanyIdAndId(userDetails.getUsername(),id);

			return ResponseEntity.ok(parking);
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Añadir un nuevo parking", description = "Añade un nuevo parking a la empresa "
			+ "Disponible  ADMIN.", security = { @SecurityRequirement(name = "bearerAuth") }, parameters = {
					@Parameter(name = "Accept-Language", description = "Idioma de la respuesta (es, en, eu)", in = ParameterIn.HEADER, required = false) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Parkings añadido correctamente"),
			@ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
			@ApiResponse(responseCode = "404", description = "Usuario erroneo") })
	public ResponseEntity<?> addParkingsToCompany(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails , @Valid @RequestBody ParkingRequest request, BindingResult result) {
		try {
			if (result.hasErrors()) {
    	        List<String> errores = result.getAllErrors().stream()
    	            .map(ObjectError::getDefaultMessage)
    	            .toList();
    	        return ResponseEntity.badRequest().body(errores);
    	    }
			// Añade un nuevo parking
			parkingService.crear(userDetails.getUsername(),request);
			return ResponseEntity.ok(messageSource.getMessage("message.ok.parking.creada", null, LocaleContextHolder.getLocale()));

		} catch (DataIntegrityViolationException ex) {
    		String mensaje = messageSource.getMessage("error.existe.parking", null, LocaleContextHolder.getLocale() );
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
	
	
	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Actualizar un parking", description = "Actualiza un parking a la empresa "
			+ "Disponible  ADMIN.", security = { @SecurityRequirement(name = "bearerAuth") }, parameters = {
					@Parameter(name = "Accept-Language", description = "Idioma de la respuesta (es, en, eu)", in = ParameterIn.HEADER, required = false) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Parkings actualizado correctamente"),
			@ApiResponse(responseCode = "403", description = "Acceso denegado. El rol no tiene permisos"),
			@ApiResponse(responseCode = "404", description = "Usuario erroneo") })
	public ResponseEntity<?> updateParkingsToCompany(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails ,  @Validated(OnUpdate.class) @RequestBody ParkingUpdateRequest request, BindingResult result) {
		try {
			if (result.hasErrors()) {
    	        List<String> errores = result.getAllErrors().stream()
    	            .map(ObjectError::getDefaultMessage)
    	            .toList();
    	        return ResponseEntity.badRequest().body(errores);
    	    }
			parkingService.update(userDetails.getUsername(),request);
			return ResponseEntity.ok(messageSource.getMessage("message.ok.parking.actualizada", null, LocaleContextHolder.getLocale()));

		} catch (DataIntegrityViolationException ex) {
    		String mensaje = messageSource.getMessage("error.existe.reserva", null, LocaleContextHolder.getLocale() );
    	    return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
    	} catch (IllegalArgumentException e) {
            logger.error("Datos no validos: {}", e.getMessage());  
            return ResponseEntity.badRequest().body(e.getMessage());
    	} catch (jakarta.validation.ConstraintViolationException e) {
    		List<String> errores = e.getConstraintViolations().stream()
    		    .map(v -> "Campo '" + v.getPropertyPath() + "' " + v.getMessage() + 
    		            " (valor: " + v.getInvalidValue() + ")").toList();
    		return ResponseEntity.badRequest().body(errores);
        }catch (RuntimeException e) {
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
