package hemen.go.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hemen.go.enums.EstadoPlaza;
import hemen.go.enums.EstadoReserva;
import hemen.go.enums.converter.EstadoReservaConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservas")
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservas_seq")
	@SequenceGenerator(name = "reservas_seq", sequenceName = "reservas_id_reserva_seq", allocationSize = 1)
	@Column(name = "id_reserva")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_persona_reserva", referencedColumnName = "id_persona")
	@JsonIgnoreProperties({ "reservas" })
	private Usuario persona;

	@ManyToOne
	@JoinColumn(name = "id_plaza_reserva", referencedColumnName = "id_plaza")
	@JsonIgnoreProperties({ "reservas" })
	private Plaza plaza;

	@Column(name = "fecha_inicio_reserva")
	private LocalDate fecInicio;

	@Column(name = "fecha_fin_reserva")
	private LocalDate fecFin;

	@Column(name = "fecha_alta_reserva")
	private LocalDate fecAlta;

	@Convert(converter = EstadoReservaConverter.class)
    @Column(name = "estado_reserva", length = 1, nullable = false)
    private EstadoReserva estado;
	
	@Column(name = "puntuacion_reserva", length = 1, nullable = false)
	private Integer puntuacion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDate getFecAlta() {
		return fecAlta;
	}

	public void setFecAlta(LocalDate fecAlta) {
		this.fecAlta = fecAlta;
	}

	public EstadoReserva getEstado() {
		return estado;
	}
	

	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}

	public void setPersona(Usuario persona) {
		this.persona = persona;
	}

	public void setPlaza(Plaza plaza) {
		this.plaza = plaza;
	}

	public Usuario getPersona() {
		return persona;
	}

	public Plaza getPlaza() {
		return plaza;
	}

	public Integer getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(Integer puntuacion) {
		this.puntuacion = puntuacion;
	}
}
