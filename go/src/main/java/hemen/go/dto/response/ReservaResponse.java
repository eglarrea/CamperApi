package hemen.go.dto.response;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservaResponse {
	private Long id;
	private Long usuarioId;
	private String usuarioEmail;
	private Long plazaId;
	private Long parkingId;
	private String parkingNombre;
	private String plazaNombre;
	private float precio;
	private float precioTotal;
	private LocalDate fecInicio;
	private LocalDate fecFin;
	private LocalDate fecAlta;
	private String estado;
	private Integer puntuacion;

	// Constructor que recibe la entidad Reserva
	public ReservaResponse(hemen.go.entity.Reserva reserva) {
		this.id = reserva.getId();

		if (reserva.getPersona() != null) {
			this.usuarioId = reserva.getPersona().getId();
			this.usuarioEmail = reserva.getPersona().getEmailPersona();
		}

		if (reserva.getPlaza() != null) {
			this.plazaId = reserva.getPlaza().getId();
			this.precio = reserva.getPlaza().getPrecio();
			this.plazaNombre = reserva.getPlaza().getNombre();
			if (reserva.getPlaza().getParking() != null) {
				this.parkingId = reserva.getPlaza().getParking().getId();
				this.parkingNombre = reserva.getPlaza().getParking().getNombre();
			}
		}

		this.fecInicio = reserva.getFecInicio();
		this.fecFin = reserva.getFecFin();
		this.fecAlta = reserva.getFecAlta();
		this.estado = reserva.getEstado();
		this.puntuacion = reserva.getPuntuacion();

		// Calcular precioTotal = precio * número de días
		if (fecInicio != null && fecFin != null && precio > 0) {
			long dias = ChronoUnit.DAYS.between(fecInicio, fecFin) + 1;
			// si la reserva es el mismo día, aseguramos al menos 1 día
			if (dias <= 0)
				dias = 1;
			this.precioTotal = precio * dias;
		} else {
			this.precioTotal = 0;
		}
	}

	// Getters
	public Long getId() {
		return id;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public String getUsuarioEmail() {
		return usuarioEmail;
	}

	public Long getPlazaId() {
		return plazaId;
	}

	public float getPrecio() {
		return precio;
	}

	public float getPrecioTotal() {
		return precioTotal;
	} // getter nuevo

	public Long getParkingId() {
		return parkingId;
	}

	public String getParkingNombre() {
		return parkingNombre;
	}

	public LocalDate getFecInicio() {
		return fecInicio;
	}

	public LocalDate getFecFin() {
		return fecFin;
	}

	public LocalDate getFecAlta() {
		return fecAlta;
	}

	public String getEstado() {
		return estado;
	}
	
	public Integer getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(Integer puntuacion) {
		this.puntuacion = puntuacion;
	}

	@Override
	public String toString() {
		return "ReservaResponse{" + "id=" + id + ", usuarioId=" + usuarioId + ", usuarioEmail='" + usuarioEmail + '\''
				+ ", plazaId=" + plazaId + ", parkingId=" + parkingId + ", parkingNombre='" + parkingNombre + '\''
				+ ", precio=" + precio + ", precioTotal=" + precioTotal + ", fecInicio=" + fecInicio + ", fecFin="
				+ fecFin + ", fecAlta=" + fecAlta + ", estado='" + estado+ ", puntuacion='" + puntuacion + '\'' + '}';
	}

	public String getPlazaNombre() {
		return plazaNombre;
	}


}
