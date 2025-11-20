package hemen.go.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table(name = "empresas")
public class Empresa {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empresa_seq")
    @SequenceGenerator(name = "empresa_seq", sequenceName = "empresa_id_empresa_seq", allocationSize = 1)
    @Column(name = "id_empresa")
    private Long id;

    @Column(name = "nombre_empresa", length = 50, nullable = false)
    private String nombreEmpresa;

    @Column(name = "cif_empresa", length = 15, nullable = false)
    private String cifEmpresa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
