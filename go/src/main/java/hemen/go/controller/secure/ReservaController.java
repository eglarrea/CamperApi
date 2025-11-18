package hemen.go.controller.secure;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;

import hemen.go.dto.request.CancelarReservaRequest;
import hemen.go.dto.request.QrRequest;
import hemen.go.dto.request.ReservaRequest;
import hemen.go.entity.Reserva;
import hemen.go.service.ReservaService;
import hemen.go.service.TokenReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reserva")
public class ReservaController {
	
	 // Logger para registrar eventos y errores
    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);
    
    private final ReservaService reservaService;
    
    private final TokenReservaService tokenReservaService;
    
    // Fuente de mensajes para internacionalización (i18n)
    private final MessageSource messageSource;

    public ReservaController( MessageSource messageSource, ReservaService reservaService, TokenReservaService tokenReservaService) {
        this.messageSource = messageSource;
        this.reservaService = reservaService;
        this.tokenReservaService = tokenReservaService;
    }
    
    @PostMapping
    @Operation(
        summary = "Realizar una reserva en un parking",
        description = "Realiza una reserva en un parking "
                    + "Si los datos son válidos "
    )
    @PreAuthorize("hasAnyRole('USER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva realizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. Los datos enviados no cumplen validaciones"),
        @ApiResponse(responseCode = "409", description = "Conflicto. Ya existe un usuario con el mismo email"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor durante el registro")
    })
    public ResponseEntity<?> reservar(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails, @Valid @RequestBody ReservaRequest request, BindingResult result) {
    	try {
    		if (result.hasErrors()) {
    	        List<String> errores = result.getAllErrors().stream()
    	            .map(ObjectError::getDefaultMessage)
    	            .toList();
    	        return ResponseEntity.badRequest().body(errores);
    	    }
    		reservaService.reservar(userDetails.getUsername(), request);
    		return ResponseEntity.ok(messageSource.getMessage("message.ok.reserva.creada", null, LocaleContextHolder.getLocale()));
    	} catch (DataIntegrityViolationException ex) {
    		String mensaje = messageSource.getMessage("error.existe.reserva", null, LocaleContextHolder.getLocale() );
    	    return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
    	} catch (IllegalArgumentException e) {
    		String mensaje = messageSource.getMessage("error.existe.reserva", null, LocaleContextHolder.getLocale() );
            logger.error("Datos no validos: {}", e.getMessage());  
            return ResponseEntity.badRequest().body(mensaje);
    	} catch (jakarta.validation.ConstraintViolationException e) {
    		List<String> errores = e.getConstraintViolations().stream()
    		    .map(v -> "Campo '" + v.getPropertyPath() + "' " + v.getMessage() + 
    		            " (valor: " + v.getInvalidValue() + ")").toList();
    		return ResponseEntity.badRequest().body(errores);
        }
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener detalle de una reserva",
        description = "Devuelve el detalle completo de una reserva por su identificador"
    )
    @PreAuthorize("hasAnyRole('USER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle de la reserva devuelto correctamente"),
        @ApiResponse(responseCode = "404", description = "La reserva no existe o no pertenece al usuario"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getReservaDetalle(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @PathVariable Long id) {
        try {
        	//TODO VER QUE DATOS NECESITAMOS PARA HACER EL REPONSE 
            Reserva reserva = reservaService.getReservaByIdAndUsuarioEmail(id, userDetails.getUsername());
           // ReservaDtoResponse response = new ReservaDtoResponse(reserva);
            return ResponseEntity.ok("ok");
        } catch (NoSuchElementException e) {
            String mensaje = messageSource.getMessage("error.reserva.no.existe", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
        }
    }
    
    @PutMapping("/cancelar")
    @Operation(
        summary = "Cancelar una reserva",
        description = "Cancela la reserva realizada"
                    + "Si los datos son válidos "
    )
    @PreAuthorize("hasAnyRole('USER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cancela la reserva correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. Los datos enviados no cumplen validaciones"),
        @ApiResponse(responseCode = "409", description = "Conflicto. Ya existe un usuario con el mismo email"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor durante el registro")
    })
    public ResponseEntity<?> candelarReservar(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails, @Valid @RequestBody CancelarReservaRequest request, BindingResult result) {
    	try {
    		if (result.hasErrors()) {
    	        List<String> errores = result.getAllErrors().stream()
    	            .map(ObjectError::getDefaultMessage)
    	            .toList();
    	        return ResponseEntity.badRequest().body(errores);
    	    }
    		
    		reservaService.cancelarReserve(userDetails.getUsername(), request.getIdReserva());
    		
    		return ResponseEntity.ok(messageSource.getMessage("message.ok.reserva.cancelada", null, LocaleContextHolder.getLocale()));
    	} catch (DataIntegrityViolationException ex) {
    		String mensaje = messageSource.getMessage("error.existe.reserva", null, LocaleContextHolder.getLocale() );
    	    return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
    	} catch (IllegalArgumentException e) {
                logger.error("Datos no validos: {}", e.getMessage());  
                String mensaje = messageSource.getMessage("error.existe.reserva", null, LocaleContextHolder.getLocale() );
                return ResponseEntity.badRequest().body(mensaje);
    	} catch (jakarta.validation.ConstraintViolationException e) {
    		List<String> errores = e.getConstraintViolations().stream()
    		    .map(v -> "Campo '" + v.getPropertyPath() + "' " + v.getMessage() + 
    		            " (valor: " + v.getInvalidValue() + ")").toList();
    		return ResponseEntity.badRequest().body(errores);
        }
    }
    
    @PostMapping("/qr")
    @Operation(
        summary = "Obtener el codigo qr de la reserva",
        description = "Realiza una reserva en un parking"
                    + "Si los datos son válidos "
    )
    @PreAuthorize("hasAnyRole('USER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva realizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. Los datos enviados no cumplen validaciones"),
        @ApiResponse(responseCode = "409", description = "Conflicto. Ya existe un usuario con el mismo email"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor durante el registro")
    })
    public ResponseEntity<?> qr(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails, @Valid @RequestBody QrRequest request, BindingResult result) {
    	try {
    		if (result.hasErrors()) {
    	        List<String> errores = result.getAllErrors().stream()
    	            .map(ObjectError::getDefaultMessage)
    	            .toList();
    	        return ResponseEntity.badRequest().body(errores);
    	    }
    		byte[] qrBytes = null;
    		try {
    			Reserva reserva=reservaService.buscarReservaPorReservaForToken(userDetails.getUsername(), request.getIdReserva());
    			String token=tokenReservaService.generarTokenPuerta(reserva.getPersona().getId(), request.getIdReserva(),reserva.getPlaza().getParking().getId());
    			qrBytes =	tokenReservaService.generarQRBytes(token);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		String qrBase64 = Base64.getEncoder().encodeToString(qrBytes);

            Map<String, Object> response = new HashMap<>();
            response.put("qrBase64", qrBase64);

            return ResponseEntity.ok(response);
    		
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
        }
    }

}
