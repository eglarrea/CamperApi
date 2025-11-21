package hemen.go.dto.request;

public class PlazaRequest {
    private String nombre;
    private boolean esVip;
    private boolean tieneElectricidad;
    private String estado;
    private float precio;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isEsVip() {
		return esVip;
	}
	public void setEsVip(boolean esVip) {
		this.esVip = esVip;
	}
	public boolean isTieneElectricidad() {
		return tieneElectricidad;
	}
	public void setTieneElectricidad(boolean tieneElectricidad) {
		this.tieneElectricidad = tieneElectricidad;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}

  
}