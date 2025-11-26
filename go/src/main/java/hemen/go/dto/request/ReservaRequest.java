package hemen.go.dto.request;

import java.time.LocalDate;

public class ReservaRequest {

	private Long idPlaza;
	private Long idParking;
	private LocalDate fecInicio;
	private LocalDate fecFin;

	public Long getIdPlaza() {
		return idPlaza;
	}

	public void setIdPlaza(Long idPlaza) {
		this.idPlaza = idPlaza;
	}

	public Long getIdParking() {
		return idParking;
	}

	public void setIdParking(Long idParking) {
		this.idParking = idParking;
	}

	public LocalDate getFecInicio() {
		return fecInicio;
	}

	public void setFecInicio(LocalDate fecInicio) {
		this.fecInicio = fecInicio;
	}

	public LocalDate getFecFin() {
		return fecFin;
	}

	public void setFecFin(LocalDate fecFin) {
		this.fecFin = fecFin;
	}

}
