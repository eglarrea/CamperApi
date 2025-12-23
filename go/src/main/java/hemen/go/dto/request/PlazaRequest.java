package hemen.go.dto.request;

public class PlazaRequest {
    private String nombre;
    private Boolean esVip;
    private Boolean tieneElectricidad;
    private String estado;
    private Float precio;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Boolean isEsVip() {
		return esVip;
	}
	public void setEsVip(Boolean esVip) {
		this.esVip = esVip;
	}
	public Boolean isTieneElectricidad() {
		return tieneElectricidad;
	}
	public void setTieneElectricidad(Boolean tieneElectricidad) {
		this.tieneElectricidad = tieneElectricidad;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Float getPrecio() {
		return precio;
	}
	public void setPrecio(Float precio) {
		this.precio = precio;
	}

  
}