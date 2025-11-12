package hemen.go.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "persona")
public class Empresa {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empresa_seq")
    @SequenceGenerator(name = "empresa_seq", sequenceName = "empresa_id_empresa_seq", allocationSize = 1)
    @Column(name = "id_empresa_persona")
    private Integer id;

    @Column(name = "nombre_empresa", length = 50, nullable = false)
    private String nombreEmpresa;

    @Column(name = "cif_empresa", length = 15, nullable = false)
    private String cifEmpresa;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public String getCifEmpresa() {
		return cifEmpresa;
	}

	public void setCifEmpresa(String cifEmpresa) {
		this.cifEmpresa = cifEmpresa;
	}
}
