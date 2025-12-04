package hemen.go.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hemen.go.entity.Reserva;

/**
 * Repositorio JPA para la entidad {@link Reserva}.
 *
 * Esta interfaz proporciona acceso a operaciones CRUD y consultas personalizadas
 * sobre la tabla de reservas. Extiende {@link JpaRepository} para operaciones básicas
 * como guardar, eliminar y buscar por ID.
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Gestionar la persistencia de reservas en la base de datos.</li>
 *   <li>Permitir consultas específicas relacionadas con solapamientos, estado y usuario.</li>
 *   <li>Facilitar búsquedas de reservas activas e históricas.</li>
 * </ul>
 */
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Busca reservas solapadas en una plaza dentro de un rango de fechas.
     *
     * <p>Uso típico:</p>
     * <ul>
     *   <li>Validar disponibilidad de una plaza antes de crear una nueva reserva.</li>
     * </ul>
     *
     * @param idPlaza identificador de la plaza.
     * @param fecInicio fecha de inicio del rango.
     * @param fecFin fecha de fin del rango.
     * @return lista de reservas que se solapan con el rango indicado.
     */
    @Query("SELECT r FROM Reserva r " +
           "WHERE r.plaza.id = :idPlaza " +
           "AND (r.fecInicio BETWEEN :fecInicio AND :fecFin " +
           "     OR r.fecFin BETWEEN :fecInicio AND :fecFin)")
    List<Reserva> findReservasSolapadas(@Param("idPlaza") Long idPlaza,
                                        @Param("fecInicio") LocalDate fecInicio,
                                        @Param("fecFin") LocalDate fecFin);

    /**
     * Busca una reserva activa de un usuario en base a su ID y el ID de la reserva.
     *
     * <p>Una reserva se considera activa si la fecha actual está entre
     * {@code fecInicio} y {@code fecFin}.</p>
     *
     * @param idUsuario identificador del usuario.
     * @param idReserva identificador de la reserva.
     * @return un {@link Optional} con la reserva activa si existe.
     */
    @Query("SELECT r FROM Reserva r " +
           "WHERE r.persona.id = :idUsuario " +
           "AND r.id = :idReserva " +
           "AND CURRENT_DATE BETWEEN r.fecInicio AND r.fecFin")
    Optional<Reserva> findReservaActiva(@Param("idUsuario") Long idUsuario,
                                        @Param("idReserva") Long idReserva);

    /**
     * Busca una reserva por su ID, el ID del usuario y el estado.
     *
     * <p>Uso típico:</p>
     * <ul>
     *   <li>Validar que una reserva pertenece a un usuario y está en un estado concreto (ej. activa).</li>
     * </ul>
     *
     * @param idReserva identificador de la reserva.
     * @param idUsuario identificador del usuario.
     * @param estado estado de la reserva (ej. "1" activa, "0" cancelada).
     * @return un {@link Optional} con la reserva si existe.
     */
    Optional<Reserva> findByIdAndPersonaIdAndEstado(Long idReserva, Long idUsuario, String estado);

    /**
     * Busca una reserva por su ID y el ID del usuario.
     *
     * @param idReserva identificador de la reserva.
     * @param idUsuario identificador del usuario.
     * @return un {@link Optional} con la reserva si existe.
     */
    Optional<Reserva> findByIdAndPersonaId(Long idReserva, Long idUsuario);

    /**
     * Obtiene todas las reservas de un usuario ordenadas por fecha de alta descendente.
     *
     * <p>Uso típico:</p>
     * <ul>
     *   <li>Mostrar el histórico de reservas de un usuario.</li>
     * </ul>
     *
     * @param usuarioId identificador del usuario.
     * @return lista de reservas ordenadas por fecha de alta (más recientes primero).
     */
    List<Reserva> findByPersonaIdOrderByFecAltaDesc(Long usuarioId);
    
    /**
     * Devuelve la media de las puntuaciones de las reservas dado el id de un parking
     *
     * 
     * @param idParking identificador del parking.
     * 
     * @return un float con la media de las reservas
     */
    @Query("SELECT AVG(r.puntuacion) FROM Reserva r INNER JOIN Plaza p ON r.plaza.id = p.id INNER JOIN Parking par ON p.parking.id = par.id " +
           "WHERE par.id = :idParking " +
            "AND r.estado = '1'")
    Float mediaReservas(@Param("idParking") Long idParking);
}
