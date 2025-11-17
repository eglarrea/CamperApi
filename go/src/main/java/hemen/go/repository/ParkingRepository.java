package hemen.go.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hemen.go.entity.Parking;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
	
	List<Parking> findbyMunicipio(String municipio);
	
	List<Parking> findbyTieneElectricidad(boolean tieneElectricidad);
	List<Parking> findbyTieneResiduales(boolean tieneResiduales);
	List<Parking> findbyTienePlazasVip(boolean tienePlazasVip);
	List<Parking> findbyTieneElectricidadAndResiduales(boolean tieneElectricidad, boolean tieneResiduales);
	List<Parking> findbyTieneElectricidadAndPlazasVip(boolean tieneElectricidad, boolean tienePlazasVip);
	List<Parking> findbyTieneResidualesAndPlazasVip(boolean tieneResiduales, boolean tienePlazasVip);
	List<Parking> findbyTieneElectricidadAndTieneResidualesAndPlazasVip(boolean tieneElectricidad,boolean tieneResiduales, boolean tienePlazasVip);
	
	List<Parking> findbyMunicipioAndTieneElectricidad(String municipio, boolean tieneElectricidad);
	List<Parking> findbyMunicipioAndTieneResiduales(String municipio, boolean tieneResiduales);
	List<Parking> findbyMunicipioAndTienePlazasVip(String municipio, boolean tienePlazasVip);
	List<Parking> findbyMunicipioAndTieneElectricidadAndResiduales(String municipio, boolean tieneElectricidad, boolean tieneResiduales);
	List<Parking> findbyMunicipioAndTieneElectricidadAndPlazasVip(String municipio, boolean tieneElectricidad, boolean tienePlazasVip);
	List<Parking> findbyMunicipioAndTieneResidualesAndPlazasVip(String municipio, boolean tieneResiduales, boolean tienePlazasVip);
	List<Parking> findbyMunicipioAndTieneElectricidadAndTieneResidualesAndPlazasVip(String municipio, boolean tieneElectricidad,boolean tieneResiduales, boolean tienePlazasVip);
	
}
