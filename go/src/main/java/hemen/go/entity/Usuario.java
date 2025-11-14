package hemen.go.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "personas")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persona_seq")
	@SequenceGenerator(
	    name = "persona_seq",
	    sequenceName = "persona_id_persona_seq",
	    allocationSize = 1
	)
	@Column(name = "id_persona")
    private Long id;

    @NotBlank(message = "{user.name.required}")
    private String nombre_persona;

    @NotBlank(message = "{user.lastname.required}")
    private String apellidos_persona;

    @NotNull(message = "{user.birthdate.required}")
    private LocalDate fec_nacimiento_persona;

    @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "{user.dni.invalid}")
    private String dni_persona;

    @Pattern(regexp = "^ES[0-9]{22}$", message = "{user.iban.invalid}")
    private String iban_persona;

    @NotBlank(message = "{user.email.required}")
    @Email(message = "{user.email.invalid}")
    @Column(name = "email_persona")
    private String emailPersona;

    @NotBlank(message ="{user.password.required}")
    private String pass_persona;
    
    private boolean is_admin;

    // Relación con la otra tabla
    @ManyToOne
    @JoinColumn(name = "id_empresa_persona", referencedColumnName = "id_empresa")
    private Empresa empresa;

    // Getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
	public String getNombre_persona() {
		return nombre_persona;
	}
	public void setNombre_persona(String nombre_persona) {
		this.nombre_persona = nombre_persona;
	}
	public String getApellidos_persona() {
		return apellidos_persona;
	}
	public void setApellidos_persona(String apellidos_persona) {
		this.apellidos_persona = apellidos_persona;
	}
	public LocalDate getFec_nacimiento_persona() {
		return fec_nacimiento_persona;
	}
	public void setFec_nacimiento_persona(LocalDate fec_nacimiento_persona) {
		this.fec_nacimiento_persona = fec_nacimiento_persona;
	}
	public String getDni_persona() {
		return dni_persona;
	}
	public void setDni_persona(String dni_persona) {
		this.dni_persona = dni_persona;
	}
	public String getIban_persona() {
		return iban_persona;
	}
	public void setIban_persona(String iban_persona) {
		this.iban_persona = iban_persona;
	}
	public String getEmailPersona() {
		return emailPersona;
	}
	public void setEmailPersona(String emailPersona) {
		this.emailPersona = emailPersona;
	}
	public String getPass_persona() {
		return pass_persona;
	}
	public void setPass_persona(String pass_persona) {
		this.pass_persona = pass_persona;
	}
	public boolean is_admin() {
		return is_admin;
	}
	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
    
}
