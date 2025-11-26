package hemen.go.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hemen.go.enums.EstadoPlaza;
import hemen.go.enums.converter.EstadoPlazaConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "plazas")
public class Plaza {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plazas_seq")
    @SequenceGenerator(name = "plazas_seq", sequenceName = "plazas_id_seq", allocationSize = 1)
    @Column(name = "id_plaza")
    private Long id;
	
	// Relación con la otra tabla
    @ManyToOne
    @JoinColumn(name = "id_parking_plaza", referencedColumnName = "id_parking")
    @JsonIgnore
    private Parking parking;
    
    @Column(name = "nombre_plaza", length = 50, nullable = false)
    private String nombre;

    @Column(name = "isvip_plaza")
    private boolean esVip;
    
    @Column(name = "tiene_electricidad_plaza")
    private boolean tieneElectricidad;
    
    
    @Convert(converter = EstadoPlazaConverter.class)
    @Column(name = "estado_plaza", length = 1, nullable = false)
    private EstadoPlaza estado;
    
    
    @Column(name = "precio_plaza")
    private float precio;
    
    @OneToMany (mappedBy = "plaza")
    private List<Reserva> reservas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * public Parking getParking() { return parking; }
	 */

	public void setParking(Parking parking) {
		this.parking = parking;
	}
	
	public Parking getParking() {
		return parking;
	}

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

	public EstadoPlaza getEstado() {
	    return estado;
	}

	public void setEstado(EstadoPlaza estado) {
	    this.estado = estado;
	}  

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}
}
