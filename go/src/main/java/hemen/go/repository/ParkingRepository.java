package hemen.go.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hemen.go.entity.Parking;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
	
	List<Parking> findBymunicipio(String municipio);
	
	List<Parking> findByTieneElectricidad(boolean tieneElectricidad);
	List<Parking> findByTieneResiduales(boolean tieneResiduales);
	List<Parking> findByTieneVips(boolean tieneVips);
	List<Parking> findByTieneElectricidadAndTieneResiduales(boolean tieneElectricidad, boolean tieneResiduales);
	List<Parking> findByTieneElectricidadAndTieneVips(boolean tieneElectricidad, boolean tieneVips);
	List<Parking> findByTieneResidualesAndTieneVips(boolean tieneResiduales, boolean tieneVips);
	List<Parking> findByTieneElectricidadAndTieneResidualesAndTieneVips(boolean tieneElectricidad,boolean tieneResiduales, boolean tieneVips);
	
	List<Parking> findByMunicipioAndTieneElectricidad(String municipio, boolean tieneElectricidad);
	List<Parking> findByMunicipioAndTieneResiduales(String municipio, boolean tieneResiduales);
	List<Parking> findByMunicipioAndTieneVips(String municipio, boolean tienePlazasVip);
	List<Parking> findByMunicipioAndTieneElectricidadAndTieneResiduales(String municipio, boolean tieneElectricidad, boolean tieneResiduales);
	List<Parking> findByMunicipioAndTieneElectricidadAndTieneVips(String municipio, boolean tieneElectricidad, boolean tienePlazasVip);
	List<Parking> findByMunicipioAndTieneResidualesAndTieneVips(String municipio, boolean tieneResiduales, boolean tienePlazasVip);
	List<Parking> findByMunicipioAndTieneElectricidadAndTieneResidualesAndTieneVips(String municipio, boolean tieneElectricidad,boolean tieneResiduales, boolean tienePlazasVip);
	
}
