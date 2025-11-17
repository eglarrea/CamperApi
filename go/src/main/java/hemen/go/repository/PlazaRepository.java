package hemen.go.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hemen.go.entity.Plaza;

public interface PlazaRepository extends JpaRepository<Plaza, Long> {
	
	@Query("SELECT p FROM Plaza p LEFT JOIN Reserva r "
			+ "  ON p.id = r.plaza.id "
			+ "  AND r.fecInicio < :fechaHasta "
			+ "  AND r.fecFin > :fechaDesde "
			+ "  AND r.estado = '1' WHERE r.id IS NULL")
	List<Plaza> findAllByPlazasLibres(@Param("fechaDesde") Date fechaDesde, @Param("fechaHasta") Date fechaHasta);

}
