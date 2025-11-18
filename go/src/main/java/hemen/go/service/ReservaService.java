package hemen.go.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hemen.go.dto.request.ReservaRequest;
import hemen.go.entity.Parking;
import hemen.go.entity.Plaza;
import hemen.go.entity.Reserva;
import hemen.go.entity.Usuario;
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
        
        List<Reserva> solapadas = reservaRepository.findReservasSolapadas(
                request.getIdPlaza(),
               
                request.getFecInicio(),
                request.getFecFin()
        );

        if (!solapadas.isEmpty()) {
            String mensaje = messageSource.getMessage(
                    "error.reserva.solapada", null, LocaleContextHolder.getLocale());
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
        reserva.setEstado("1");
        
        reserva.setFecInicio(request.getFecInicio());
        reserva.setFecFin(request.getFecFin());
        reserva.setFecAlta( new Date(new java.util.Date().getTime()));
       
        
        reservaRepository.save(reserva);

        // 2. Generar el token usando la reserva ya guardada (con id)
        /*String token = tokenReservaService.generarToken(guardada);
        guardada.setToken(token);

        // 3. Actualizar la reserva con el token
        reservaRepository.save(guardada);*/
    }
	
	/*public Reserva buscarReservaPorReservaToken(Long idUsuario, Long idReserva, String token) {
	    return reservaRepository.findByUsuarioAndReservaAndToken(idUsuario, idReserva, token)
	            .orElseThrow(() -> new IllegalArgumentException("No existe la reserva con esos datos"));
	}*/
	
	public void cancelarReserve(String email, Long idReserva) {
		Usuario user = usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new UsernameNotFoundException( messageSource.getMessage(
                        "error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
		Reserva reserva = reservaRepository.findByIdAndPersonaId( idReserva,user.getId())
        .orElseThrow(() -> new IllegalArgumentException("No existe la reserva con esos datos"));
		reserva.setEstado("0");
		reservaRepository.save(reserva);
	}
	
	public Reserva buscarReservaPorReservaForToken(String email, Long idReserva) {
		
		Usuario user = usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new UsernameNotFoundException( messageSource.getMessage(
                        "error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
	    return reservaRepository.findReservaActiva(user.getId(), idReserva)
	            .orElseThrow(() -> new IllegalArgumentException("No existe la reserva con esos datos"));
	}
}
