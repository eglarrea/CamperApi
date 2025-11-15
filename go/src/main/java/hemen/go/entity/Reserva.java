package hemen.go.entity;

import java.sql.Date;

import jakarta.persistence.Column;
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
	private Usuario persona;
	 
	@ManyToOne
	@JoinColumn(name = "id_plaza_reserva", referencedColumnName = "id_plaza")
	private Plaza plaza;
	 
	@Column(name="fecha_inicio_reserva")
	private Date fecInicio;
	
	@Column(name="fecha_fin_reserva")
	private Date fecFin;
	
	@Column(name="fecha_alta_reserva")
	private Date fecAlta;
	
	@Column(name = "estado_reserva", length = 1, nullable = false)
	private String estado;
	
	@Column(name = "token_reserva", length = 100, nullable = false)
	private String token;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecInicio() {
		return fecInicio;
	}

	public void setFecInicio(Date fecInicio) {
		this.fecInicio = fecInicio;
	}

	public Date getFecFin() {
		return fecFin;
	}

	public void setFecFin(Date fecFin) {
		this.fecFin = fecFin;
	}

	public Date getFecAlta() {
		return fecAlta;
	}

	public void setFecAlta(Date fecAlta) {
		this.fecAlta = fecAlta;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setPersona(Usuario persona) {
		this.persona = persona;
	}

	public void setPlaza(Plaza plaza) {
		this.plaza = plaza;
	}
}
