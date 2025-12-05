package hemen.go.dto.response;

import java.time.LocalDate;
import java.util.List;

import hemen.go.entity.Parking;
import hemen.go.entity.Plaza;

public class ParkingDtoFindResponse {
	 private Long id;
	    private String nombre;
	    private String provincia;
	    private String municipio;
	    private boolean isActivo;
	    private String web;
	    private String telefono;
	    private String email;
	    private String personaContacto;
	    private boolean tieneElectricidad;
	    private boolean tieneResiduales;
	    private boolean tieneVips;
	    private Float media;
	    private List<PlazaResponse> plazas;

	    public ParkingDtoFindResponse(Parking parking, LocalDate fechaDesde, LocalDate fechaHasta) {
	        this.id = parking.getId();
	        this.nombre = parking.getNombre();
	        this.provincia = parking.getProvincia();
	        this.municipio = parking.getMunicipio();
	        this.isActivo = parking.isActivo();
	        this.web = parking.getWeb();
	        this.telefono = parking.getTelefono();
	        this.email = parking.getEmail();
	        this.personaContacto = parking.getPersonaContacto();
	        this.tieneElectricidad = parking.isTieneElectricidad();
	        this.tieneResiduales = parking.isTieneResiduales();
	        this.tieneVips = parking.isTieneVips();

	        // filtrar plazas libres
	        this.plazas = parking.getPlazas().stream()
	            .filter(plaza -> plazaLibre(plaza, fechaDesde, fechaHasta))
	            .map(PlazaResponse::new)
	            .toList();
	    }

	    private boolean plazaLibre(Plaza plaza, LocalDate fechaDesde, LocalDate fechaHasta) {
	    	if (fechaDesde == null || fechaHasta == null) {
	            return true;
	        }
	        return plaza.getReservas().stream()
	            .noneMatch(r -> "1".equals(r.getEstado())
	                && !r.getFecInicio().isAfter(fechaHasta)
	                && !r.getFecFin().isBefore(fechaDesde));
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getProvincia() {
			return provincia;
		}

		public void setProvincia(String provincia) {
			this.provincia = provincia;
		}

		public String getMunicipio() {
			return municipio;
		}

		public void setMunicipio(String municipio) {
			this.municipio = municipio;
		}

		public boolean isActivo() {
			return isActivo;
		}

		public void setActivo(boolean isActivo) {
			this.isActivo = isActivo;
		}

		public String getWeb() {
			return web;
		}

		public void setWeb(String web) {
			this.web = web;
		}

		public String getTelefono() {
			return telefono;
		}

		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPersonaContacto() {
			return personaContacto;
		}

		public void setPersonaContacto(String personaContacto) {
			this.personaContacto = personaContacto;
		}

		public boolean isTieneElectricidad() {
			return tieneElectricidad;
		}

		public void setTieneElectricidad(boolean tieneElectricidad) {
			this.tieneElectricidad = tieneElectricidad;
		}

		public boolean isTieneResiduales() {
			return tieneResiduales;
		}

		public void setTieneResiduales(boolean tieneResiduales) {
			this.tieneResiduales = tieneResiduales;
		}

		public boolean isTieneVips() {
			return tieneVips;
		}

		public void setTieneVips(boolean tieneVips) {
			this.tieneVips = tieneVips;
		}

		public List<PlazaResponse> getPlazas() {
			return plazas;
		}

		public void setPlazas(List<PlazaResponse> plazas) {
			this.plazas = plazas;
		}

		public Float getMedia() {
			return media;
		}

		public void setMedia(Float media) {
			this.media = media;
		}
}
