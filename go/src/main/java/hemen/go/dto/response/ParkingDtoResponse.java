package hemen.go.dto.response;

import java.util.List;
import java.util.stream.Collectors;

public class ParkingDtoResponse {

	 private Long id;
	 private String nombre;
	 private String localidad;
	 private String provincia;
	 private String web;
	 private String telefono;
	 private String email;
	 private boolean tomaElectricidad;
	 private boolean limpiezaAguasResiduales;
	 private boolean plazasVip;
	 private Integer numeroPlazas;
	 private boolean isActivoParking;
	 private Float media;
	 private List<PlazaResponse> plazasResponse;
	 
	 public ParkingDtoResponse(){}
	 
	 public ParkingDtoResponse(hemen.go.entity.Parking parking){
		 
		 this.id =parking.getId();
		 this.limpiezaAguasResiduales= parking.isTieneResiduales();
		 this.plazasVip=parking.isTieneElectricidad();
		 this.tomaElectricidad=parking.isTieneElectricidad();
		 this.nombre= parking.getNombre();
		 this.localidad=parking.getMunicipio();
		 this.provincia = parking.getProvincia();
		 this.web = parking.getWeb();
	     this.telefono = parking.getTelefono();
	     this.email = parking.getEmail();
	     this.isActivoParking= parking.isActivo();
		 this.numeroPlazas=0;
		 if (parking.getPlazas() != null && !parking.getPlazas().isEmpty()) {
	            this.numeroPlazas = parking.getPlazas().size();
	            this.plazasResponse = parking.getPlazas()
	                                         .stream()
	                                         .map(PlazaResponse::new) // usar constructor DTO
	                                         .collect(Collectors.toList());
	        } else {
	            this.numeroPlazas = 0;
	            this.plazasResponse = List.of(); // lista vacía
	        }
		 
	 }
	 
	 public ParkingDtoResponse(Long id, String nombre, String localidad, boolean tomaElectricidad,
			boolean limpiezaAguasResiduales, boolean plazasVip) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.localidad = localidad;
		this.tomaElectricidad = tomaElectricidad;
		this.limpiezaAguasResiduales = limpiezaAguasResiduales;
		this.plazasVip = plazasVip;
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

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public boolean isTomaElectricidad() {
		return tomaElectricidad;
	}

	public void setTomaElectricidad(boolean tomaElectricidad) {
		this.tomaElectricidad = tomaElectricidad;
	}

	public boolean isLimpiezaAguasResiduales() {
		return limpiezaAguasResiduales;
	}

	public void setLimpiezaAguasResiduales(boolean limpiezaAguasResiduales) {
		this.limpiezaAguasResiduales = limpiezaAguasResiduales;
	}

	public boolean isPlazasVip() {
		return plazasVip;
	}

	public void setPlazasVip(boolean plazasVip) {
		this.plazasVip = plazasVip;
	}

	public Integer getNumeroPlazas() {
		return numeroPlazas;
	}

	public void setNumeroPlazas(Integer numeroPlazas) {
		this.numeroPlazas = numeroPlazas;
	}

	public List<PlazaResponse> getPlazasResponse() {
		return plazasResponse;
	}

	public void setPlazasResponse(List<PlazaResponse> plazasResponse) {
		this.plazasResponse = plazasResponse;
	}

	public Float getMedia() {
		return media;
	}

	public void setMedia(Float media) {
		this.media = media;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
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

	public boolean isActivoParking() {
		return isActivoParking;
	}

	public void setActivoParking(boolean isActivoParking) {
		this.isActivoParking = isActivoParking;
	}
}
