package hemen.go.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hemen.go.dto.request.ReservaRequest;
import hemen.go.dto.response.UserDtoResponse;
import hemen.go.entity.Parking;
import hemen.go.entity.Plaza;
import hemen.go.entity.Reserva;
import hemen.go.entity.Usuario;
import hemen.go.repository.ParkingRepository;
import hemen.go.repository.ReservaRepository;
import hemen.go.repository.UsuarioRepository;

@Service
public class ReservaService {
	
	private final UsuarioRepository usuarioRepository;
	private final ReservaRepository reservaRepository;
	private final MessageSource messageSource;
	private final TokenReservaService tokenReservaService;
	
	public ReservaService(UsuarioRepository usuarioRepository, ReservaRepository reservaRepository,  MessageSource messageSource, TokenReservaService tokenReservaService) {
        this.usuarioRepository = usuarioRepository;
        this.messageSource = messageSource;
        this.reservaRepository = reservaRepository;
        this.tokenReservaService = tokenReservaService;
    }
	
	public void reservar(String email, ReservaRequest request) {
        Usuario user = usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new UsernameNotFoundException( messageSource.getMessage(
                        "error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
        
        if( null==user.getIban_persona() || user.getIban_persona().trim().equals("")) {
        	String mensaje = messageSource.getMessage(
                    "user.iban.invalid", null, LocaleContextHolder.getLocale());
           throw new IllegalArgumentException(mensaje);
        }
        Reserva reserva = new Reserva();
        Plaza plaza = new Plaza();
        Parking parking= new Parking();
        parking.setId(request.getIdParking());
        plaza.setId(request.getIdPlaza());
        
        plaza.setParking(parking);
        reserva.setPersona(user);
        reserva.setPlaza(plaza);
        
        reserva.setFecInicio(request.getFecInicio());
        reserva.setFecFin(request.getFecFin());
        
        Reserva guardada = reservaRepository.save(reserva);

        // 2. Generar el token usando la reserva ya guardada (con id)
        String token = tokenReservaService.generarToken(guardada);
        guardada.setToken(token);

        // 3. Actualizar la reserva con el token
        reservaRepository.save(guardada);
    }
}
