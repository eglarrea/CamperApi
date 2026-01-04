package hemen.go.service;

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

import hemen.go.dto.request.ReservaRequest;
import hemen.go.dto.response.ReservaResponse;
import hemen.go.entity.Parking;
import hemen.go.entity.Plaza;
import hemen.go.entity.Reserva;
import hemen.go.entity.Usuario;
import hemen.go.repository.ReservaRepository;
import hemen.go.repository.UsuarioRepository;
import hemen.go.validator.FechaValidator;

/**
 * Servicio de gestión de reservas para la aplicación Hemengo.
 *
 * Centraliza la lógica de negocio para:
 * - Crear nuevas reservas con validación de fechas, usuario y solapes.
 * - Cancelar reservas siguiendo la política de cancelación configurable.
 * - Consultar reservas activas e históricas de un usuario.
 *
 * Notas de negocio:
 * - El estado "1" se considera reserva activa; "0" cancelada.
 * - La política de cancelación se evalúa con {@code diasCancelacion} días
 *   tanto por antelación a la fecha de inicio como por antigüedad de la reserva.
 */
@Service
public class ReservaService {

   
    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;
    private final FechaValidator fechaValidator;
    private final MessageSource messageSource;

    /**
     * Número de días de política de cancelación.
     * Inyectado desde la configuración con la clave {@code reserva.cancelacion.dias}.
     *
     * Reglas aplicadas:
     * - Se permite cancelar si faltan al menos {@code diasCancelacion} días para el inicio, o
     * - Si la reserva se realizó hace {@code diasCancelacion} días o menos.
     */
    @Value("${reserva.cancelacion.dias}")
    private int diasCancelacion;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioRepository repositorio de usuarios.
     * @param reservaRepository repositorio de reservas.
     * @param fechaValidator validador de fechas para reservas.
     * @param messageSource fuente de mensajes internacionalizados.
     */
    public ReservaService(UsuarioRepository usuarioRepository,
                          ReservaRepository reservaRepository,
                          FechaValidator fechaValidator,
                          MessageSource messageSource) {
        this.usuarioRepository = usuarioRepository;
        this.messageSource = messageSource;
        this.reservaRepository = reservaRepository;
        this.fechaValidator = fechaValidator;
    }

    /**
     * Registra una nueva reserva para el usuario indicado.
     *
     * Flujo:
     * 1) Verifica que el usuario exista y tenga IBAN registrado.
     * 2) Valida fechas de inicio y fin mediante {@link FechaValidator}.
     * 3) Comprueba solapes en la plaza para el rango indicado.
     * 4) Crea y persiste la reserva como activa.
     *
     * @param email correo electrónico del usuario que realiza la reserva.
     * @param request datos de la reserva: idParking, idPlaza, fechas de inicio y fin.
     * @throws UsernameNotFoundException si el usuario no existe.
     * @throws IllegalArgumentException si el IBAN es inválido, las fechas no son válidas
     *                                  o existen reservas solapadas.
     */
    public void reservar(String email, ReservaRequest request) {
        Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

        if (user.getIban_persona() == null || user.getIban_persona().trim().isEmpty()) {
            String mensaje = messageSource.getMessage("user.iban.invalid", null, LocaleContextHolder.getLocale());
            throw new IllegalArgumentException(mensaje);
        }

        fechaValidator.validarFechas(request.getFecInicio(), request.getFecFin());

        List<Reserva> solapadas = reservaRepository.findReservasSolapadas(
                request.getIdPlaza(), request.getFecInicio(), request.getFecFin());

        if (!solapadas.isEmpty()) {
            String mensaje = messageSource.getMessage("error.reserva.solapada", null, LocaleContextHolder.getLocale());
            throw new IllegalArgumentException(mensaje);
        }

        Reserva reserva = new Reserva();
        Plaza plaza = new Plaza();
        Parking parking = new Parking();
        parking.setId(request.getIdParking());
        plaza.setId(request.getIdPlaza());
        plaza.setParking(parking);

        reserva.setPersona(user);
        reserva.setPlaza(plaza);
        reserva.setEstado("1"); // Activa
        reserva.setFecInicio(request.getFecInicio());
        reserva.setFecFin(request.getFecFin());
        reserva.setFecAlta(LocalDate.now());

        reservaRepository.save(reserva);
    }

    /**
     * Cancela una reserva activa del usuario si cumple la política de cancelación.
     *
     * Política:
     * - Antelación: hoy + diasCancelacion &lt; fechaInicio.
     * - Reciente: días entre fechaAlta y hoy ≤ diasCancelacion.
     *
     * Si no se cumple alguna de las dos condiciones, la cancelación no es válida.
     *
     * @param email correo electrónico del usuario propietario de la reserva.
     * @param idReserva identificador de la reserva a cancelar.
     * @throws UsernameNotFoundException si el usuario no existe.
     * @throws IllegalArgumentException si la reserva no existe, no está activa
     *                                  o no cumple la política de cancelación.
     */
    public void cancelarReserva(String email, Long idReserva) {
        Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

        Reserva reserva = reservaRepository.findByIdAndPersonaIdAndEstado(idReserva, user.getId(), "1")
                .orElseThrow(() -> new IllegalArgumentException("No existe la reserva con esos datos"));

        LocalDate hoy = LocalDate.now();
        LocalDate fechaInicio = reserva.getFecInicio();
        LocalDate fechaAlta = reserva.getFecAlta();

        logger.error("Dias cancelacion=" + diasCancelacion);

     // 1. No permitir cancelar si la estancia es hoy o ya ha comenzado
        if (!hoy.isBefore(fechaInicio)) {
            logger.error("No se puede cancelar porque la estancia es hoy o ya ha comenzado");
            throw new IllegalArgumentException(
                messageSource.getMessage(
                    "error.reserva.estancia.iniciada",
                    null,
                    LocaleContextHolder.getLocale()
                )
            );
        }

        // 2. Reglas de cancelación
        boolean cumpleAntelacion = hoy.plusDays(diasCancelacion).isBefore(fechaInicio);
        boolean cumpleReciente = ChronoUnit.DAYS.between(fechaAlta, hoy) <= diasCancelacion;

        if (!(cumpleAntelacion || cumpleReciente)) {
            logger.error("No se puede cancelar cumpleAntelacion:" + cumpleAntelacion +" cumpleReciente:"+cumpleReciente);
            throw new IllegalArgumentException(
                messageSource.getMessage(
                    "error.reserva.cancelacion",
                    new Object[]{diasCancelacion, diasCancelacion},
                    LocaleContextHolder.getLocale()
                )
            );
        }


        reserva.setEstado("0"); // Cancelada
        reservaRepository.save(reserva);
    }
    
    
    public void puntuarReserve(String email, Long idReserva,Integer puntuacion) {
    	Usuario user = usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

        Reserva reserva = reservaRepository.findByIdAndPersonaId(idReserva, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("No existe la reserva con esos datos"));

        LocalDate hoy = LocalDate.now();
        LocalDate fechaFin = reserva.getFecFin();

        // 1. Comprobar que la reserva ya terminó
        if (hoy.isBefore(fechaFin)) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "error.reserva.no.finalizada",
                    null,
                    LocaleContextHolder.getLocale()
            ));
        }

        // 2. Comprobar que no ha sido puntuada antes
        if (reserva.getPuntuacion() != null) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "error.reserva.ya.puntuada",
                    null,
                    LocaleContextHolder.getLocale()
            ));
        }
        
        if (puntuacion < 0 || puntuacion > 10) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "error.reserva.puntuacion.rango",
                    new Object[]{0, 10},
                    LocaleContextHolder.getLocale()
            ));
        }

        // 3. Guardar la puntuación
        reserva.setPuntuacion(puntuacion);
        reservaRepository.save(reserva);
    }

    /**
     * Busca una reserva activa por usuario y su identificador.
     *
     * Nota: El “token” no se gestiona aquí; se asume que {@link ReservaRepository#findReservaActiva}
     * aplica los criterios necesarios (p. ej., estado y pertenencia).
     *
     * @param email correo del usuario.
     * @param idReserva identificador de la reserva.
     * @return la reserva activa si existe.
     * @throws UsernameNotFoundException si el usuario no existe.
     * @throws IllegalArgumentException si no existe reserva activa con esos datos.
     */
    public Reserva buscarReservaPorReservaForToken(String email, Long idReserva) {
        Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
        return reservaRepository.findReservaActiva(user.getId(), idReserva)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("error.reserva.no.existe", null, LocaleContextHolder.getLocale())));
    }

    /**
     * Obtiene una reserva por su ID y el email del usuario.
     *
     * @param idReserva identificador de la reserva.
     * @param emailUsuario correo electrónico del usuario.
     * @return la reserva encontrada.
     * @throws UsernameNotFoundException si el usuario no existe.
     * @throws NoSuchElementException si no se encuentra la reserva para ese usuario.
     */
    public Reserva getReservaByIdAndUsuarioEmail(Long idReserva, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmailPersona(emailUsuario)
                .orElseThrow(() ->  new UsernameNotFoundException(
                        messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

        return reservaRepository.findByIdAndPersonaId(idReserva, usuario.getId())
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("error.reserva.no.existe", null, LocaleContextHolder.getLocale())));
    }

    /**
     * Devuelve el histórico de reservas de un usuario, ordenado por fecha de alta descendente.
     *
     * Conversión:
     * - Cada {@link Reserva} se transforma a {@link ReservaResponse} para exponer solo los datos necesarios al cliente.
     *
     * @param emailUsuario correo electrónico del usuario.
     * @return lista de respuestas de reservas históricas.
     * @throws IllegalArgumentException si el usuario no existe.
     */
    public List<ReservaResponse> getHistoricoReservas(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmailPersona(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<Reserva> reservas = reservaRepository.findByPersonaIdOrderByFecAltaDesc(usuario.getId());
        return reservas.stream().map(ReservaResponse::new).toList();
    }
}
