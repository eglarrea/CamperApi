package hemen.go.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hemen.go.controller.secure.ReservaController;
import hemen.go.dto.request.ReservaRequest;
import hemen.go.dto.response.ReservaResponse;
import hemen.go.entity.Parking;
import hemen.go.entity.Plaza;
import hemen.go.entity.Reserva;
import hemen.go.entity.Usuario;
import hemen.go.repository.ReservaRepository;
import hemen.go.repository.UsuarioRepository;

@Service
public class ReservaService {
	private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);
	private final UsuarioRepository usuarioRepository;
	private final ReservaRepository reservaRepository;
	private final MessageSource messageSource;
	
	@Value("${reserva.cancelacion.dias}")
	private int diasCancelacion;
	
	public ReservaService(UsuarioRepository usuarioRepository, ReservaRepository reservaRepository,  MessageSource messageSource) {
        this.usuarioRepository = usuarioRepository;
        this.messageSource = messageSource;
        this.reservaRepository = reservaRepository;
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
    }
	
	/*public Reserva buscarReservaPorReservaToken(Long idUsuario, Long idReserva, String token) {
	    return reservaRepository.findByUsuarioAndReservaAndToken(idUsuario, idReserva, token)
	            .orElseThrow(() -> new IllegalArgumentException("No existe la reserva con esos datos"));
	}*/
	
	/*public void cancelarReserve(String email, Long idReserva) {
		Usuario user = usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new UsernameNotFoundException( messageSource.getMessage(
                        "error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
		Reserva reserva = reservaRepository.findByIdAndPersonaIdAndEstado( idReserva,user.getId(),"1")
        .orElseThrow(() -> new IllegalArgumentException("No existe la reserva con esos datos"));
		reserva.setEstado("0");
		reservaRepository.save(reserva);
	}*/
	public void cancelarReserve(String email, Long idReserva) {
	 Usuario user = usuarioRepository.findByEmailPersona(email)
	            .orElseThrow(() -> new UsernameNotFoundException(
	                    messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

	    Reserva reserva = reservaRepository.findByIdAndPersonaIdAndEstado(idReserva, user.getId(), "1")
	            .orElseThrow(() -> new IllegalArgumentException("No existe la reserva con esos datos"));

	    LocalDate hoy = LocalDate.now();

		 // Si fecInicio y fecAlta son java.sql.Date
		 LocalDate fechaInicio = reserva.getFecInicio().toLocalDate();
		 LocalDate fechaAlta   = reserva.getFecAlta().toLocalDate();

		logger.error("Dias cancelacion="+diasCancelacion);
	    // Condición 1: faltan al menos X días para inicio
	    boolean cumpleAntelacion = hoy.plusDays(diasCancelacion).isBefore(fechaInicio);

	    // Condición 2: la reserva se hizo hace <= 6 días
	    boolean cumpleReciente = ChronoUnit.DAYS.between(fechaAlta, hoy) <= diasCancelacion;

	    if (!(cumpleAntelacion || cumpleReciente)) {
	        throw new IllegalArgumentException(messageSource.getMessage("error.reserva.cancelacion",
	                new Object[]{diasCancelacion, diasCancelacion},
	                LocaleContextHolder.getLocale()));
	    }

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
	
	public Reserva getReservaByIdAndUsuarioEmail(Long idReserva, String emailUsuario) {
	    Usuario usuario = usuarioRepository.findByEmailPersona(emailUsuario)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

	    return reservaRepository.findByIdAndPersonaId(idReserva, usuario.getId())
	            .orElseThrow(() -> new NoSuchElementException("Reserva no encontrada"));
	}
	
	public List<ReservaResponse> getHistoricoReservas(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmailPersona(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<Reserva> reservas = reservaRepository.findByPersonaIdOrderByFecAltaDesc(usuario.getId());

        return reservas.stream()
                .map(ReservaResponse::new) // convierte cada Reserva en ReservaResponse
                .toList();
    }
}
