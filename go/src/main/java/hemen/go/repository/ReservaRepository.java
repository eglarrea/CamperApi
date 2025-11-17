package hemen.go.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hemen.go.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
	
	
	@Query("SELECT r FROM Reserva r " +
		       "WHERE r.plaza.id = :idPlaza " +
		       "AND r.plaza.parking.id = :idParking " +
		       "AND r.fecInicio < :fecFin " +
		       "AND r.fecFin > :fecInicio")
	List<Reserva> findReservasSolapadas(@Param("idPlaza") Long idPlaza, @Param("idParking") Long idParking,
		                                    @Param("fecInicio") Date fecInicio, @Param("fecFin") Date fecFin);
}
