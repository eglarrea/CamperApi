package hemen.go.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hemen.go.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
	
	
	@Query("SELECT r FROM Reserva r " +
		       "WHERE r.plaza.id = :idPlaza " +
		       "AND (r.fecInicio BETWEEN :fecInicio AND :fecFin " +
		       "     OR r.fecFin BETWEEN :fecInicio AND :fecFin)")
	List<Reserva> findReservasSolapadas(@Param("idPlaza") Long idPlaza, 
		                                    @Param("fecInicio") Date fecInicio, @Param("fecFin") Date fecFin);
	
	@Query("SELECT r FROM Reserva r " +
		       "WHERE r.persona.id = :idUsuario " +
		       "AND r.id = :idReserva " +
		       "AND CURRENT_DATE BETWEEN r.fecInicio AND r.fecFin")
	Optional<Reserva> findReservaActiva(@Param("idUsuario") Long idUsuario,
		                                    @Param("idReserva") Long idReserva);
	
	Optional<Reserva> findByIdAndPersonaId(Long idReserva, Long idUsuario);
}
