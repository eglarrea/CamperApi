package hemen.go.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hemen.go.entity.Parking;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
	
	List<Parking> findAllBymunicipio(String municipio);
	
	List<Parking> findAllByTieneElectricidad(boolean tieneElectricidad);
	List<Parking> findAllByTieneResiduales(boolean tieneResiduales);
	List<Parking> findAllByTieneVips(boolean tieneVips);
	List<Parking> findAllByTieneElectricidadAndTieneResiduales(boolean tieneElectricidad, boolean tieneResiduales);
	List<Parking> findAllByTieneElectricidadAndTieneVips(boolean tieneElectricidad, boolean tieneVips);
	List<Parking> findAllByTieneResidualesAndTieneVips(boolean tieneResiduales, boolean tieneVips);
	List<Parking> findAllByTieneElectricidadAndTieneResidualesAndTieneVips(boolean tieneElectricidad,boolean tieneResiduales, boolean tieneVips);
	
	List<Parking> findAllByMunicipioAndTieneElectricidad(String municipio, boolean tieneElectricidad);
	List<Parking> findAllByMunicipioAndTieneResiduales(String municipio, boolean tieneResiduales);
	List<Parking> findAllByMunicipioAndTieneVips(String municipio, boolean tienePlazasVip);
	List<Parking> findAllByMunicipioAndTieneElectricidadAndTieneResiduales(String municipio, boolean tieneElectricidad, boolean tieneResiduales);
	List<Parking> findAllByMunicipioAndTieneElectricidadAndTieneVips(String municipio, boolean tieneElectricidad, boolean tienePlazasVip);
	List<Parking> findAllByMunicipioAndTieneResidualesAndTieneVips(String municipio, boolean tieneResiduales, boolean tienePlazasVip);
	List<Parking> findAllByMunicipioAndTieneElectricidadAndTieneResidualesAndTieneVips(String municipio, boolean tieneElectricidad,boolean tieneResiduales, boolean tienePlazasVip);
	
	List<Parking> findAllByPlazasReservasFecInicioBetween(Date fechaDesde, Date fechaHasta);
	List<Parking> findAllByPlazasReservasFecFinBetween(Date fechaDesde, Date fechaHasta);
	List<Parking> findAllByPlazasReservasFecInicioBetweenOrPlazasReservasFecFinBetween(Date fechaInicioDesde, Date fechaInicioHasta, Date fechaFinDesde, Date fechaFinHasta);
	
	@Query("SELECT pk FROM Parking pk INNER JOIN Plaza pl ON pk.id = pl.parking.id " + 
			"LEFT JOIN Reserva r ON pl.id = r.plaza.id " +
			" WHERE r.fecInicio IS NULL OR (r.estado = '1' AND r.fecInicio NOT BETWEEN :fechaDesde AND :fechaHasta " +
			" AND r.fecFin NOT BETWEEN :fechaDesde AND :fechaHasta)") 
	List<Parking> findAllByPlazasLibres(@Param("fechaDesde") Date fechaDesde, @Param("fechaHasta") Date fechaHasta);
}
