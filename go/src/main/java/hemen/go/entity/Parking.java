package hemen.go.entity;

import java.util.List;

import jakarta.persistence.Column;
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
@Table(name = "parkings")
public class Parking {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parking_seq")
    @SequenceGenerator(name = "parking_seq", sequenceName = "parking_id_seq", allocationSize = 1)
    @Column(name = "id_parking")
    private Long id;
	
	// Relación con la otra tabla
    @ManyToOne
    @JoinColumn(name = "id_empresa_parking", referencedColumnName = "id_empresa")
    private Empresa empresa;
    
    @Column(name = "nombre_parking", length = 100, nullable = false)
    private String nombre;
    
    @Column(name = "provincia_parking")
    private String provincia;
    
    @Column(name = "municipio_parking")
    private String municipio;
    
    @Column(name = "isactivo_parking")
    private boolean isActivo;
    
    @Column(name = "web_parking")
    private String web;
    
    @Column(name = "telefono_parking")
    private String telefono;
    
    @Column(name = "email_parking")
    private String email;
    
    @Column(name = "persona_contacto_parking")
    private String personaContacto;
    
    @Column(name = "tiene_electricidad_parking")
    private boolean tieneElectricidad;
    
    @Column(name = "tiene_residuales_parking")
    private boolean tieneResiduales;
    
    @Column(name = "tiene_plazas_vip_parking")
    private boolean tienePlazasVip;

    @OneToMany (mappedBy = "parking")
    private List<Plaza> plazas;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public boolean isActivo() {
		return isActivo;
	}

	public void setActivo(boolean isActivo) {
		this.isActivo = isActivo;
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

	public String getPersonaContacto() {
		return personaContacto;
	}

	public void setPersonaContacto(String personaContacto) {
		this.personaContacto = personaContacto;
	}

	public boolean isTieneElectricidad() {
		return tieneElectricidad;
	}

	public void setTieneElectricidad(boolean tieneElectricidad) {
		this.tieneElectricidad = tieneElectricidad;
	}

	public boolean isTieneResiduales() {
		return tieneResiduales;
	}

	public void setTieneResiduales(boolean tieneResiduales) {
		this.tieneResiduales = tieneResiduales;
	}

	public boolean isTienePlazasVip() {
		return tienePlazasVip;
	}

	public void setTienePlazasVip(boolean tienePlazasVip) {
		this.tienePlazasVip = tienePlazasVip;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public List<Plaza> getPlazas() {
		return plazas;
	}

	public void setPlazas(List<Plaza> plazas) {
		this.plazas = plazas;
	}
}
