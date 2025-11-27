package hemen.go.dto.request;

import java.time.LocalDate;

public class FilterParkingRequest {
	// Rango de fechas
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    // Localidad del parking
    private String localidad;
    
    private String provincia;

    // Servicios adicionales
    private Boolean tomaElectricidad;
    private Boolean limpiezaAguasResiduales;
    private Boolean plazasVip;

    // Getters y Setters
    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Boolean isTomaElectricidad() {
        return tomaElectricidad;
    }

    public void setTomaElectricidad(Boolean tomaElectricidad) {
        this.tomaElectricidad = tomaElectricidad;
    }

    public Boolean isLimpiezaAguasResiduales() {
        return limpiezaAguasResiduales;
    }

    public void setLimpiezaAguasResiduales(Boolean limpiezaAguasResiduales) {
        this.limpiezaAguasResiduales = limpiezaAguasResiduales;
    }

    public Boolean isPlazasVip() {
        return plazasVip;
    }

    public void setPlazasVip(Boolean plazasVip) {
        this.plazasVip = plazasVip;
    }

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
}
