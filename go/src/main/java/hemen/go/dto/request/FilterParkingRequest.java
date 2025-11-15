package hemen.go.dto.request;

import java.time.LocalDate;

public class FilterParkingRequest {
	// Rango de fechas
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    // Localidad del parking
    private String localidad;

    // Servicios adicionales
    private boolean tomaElectricidad;
    private boolean limpiezaAguasResiduales;
    private boolean plazasVip;

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
}
