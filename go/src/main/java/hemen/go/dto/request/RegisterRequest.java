package hemen.go.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class RegisterRequest {

	@NotBlank(message = "{user.name.required}")
    private String nombrePersona;

	@NotBlank(message = "{user.lastname.required}")
    private String apellidosPersona;

	@NotNull(message = "{user.birthdate.required}")
	@Past(message = "{user.birthdate.past}")
    private LocalDate fecNacimientoPersona;

	@Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "{user.dni.invalid}")
    private String dniPersona;

	@Pattern(regexp = "^ES[0-9]{22}$", message = "{user.iban.invalid}")
    private String ibanPersona;

	@NotBlank(message = "{user.email.required}")
	@Email(message = "{user.email.invalid}")
    private String emailPersona;

	@NotBlank(message = "{user.password.required}")
    private String passPersona;

	@NotBlank(message = "{user.password.confirm}")
    private String confirmPassPersona;

    private boolean isAdmin;

    //private Long empresaId;

    // Getters y setters
    public String getNombrePersona() { return nombrePersona; }
    public void setNombrePersona(String nombrePersona) { this.nombrePersona = nombrePersona; }

    public String getApellidosPersona() { return apellidosPersona; }
    public void setApellidosPersona(String apellidosPersona) { this.apellidosPersona = apellidosPersona; }

    public LocalDate getFecNacimientoPersona() { return fecNacimientoPersona; }
    public void setFecNacimientoPersona(LocalDate fecNacimientoPersona) { this.fecNacimientoPersona = fecNacimientoPersona; }

    public String getDniPersona() { return dniPersona; }
    public void setDniPersona(String dniPersona) { this.dniPersona = dniPersona; }

    public String getIbanPersona() { return ibanPersona; }
    public void setIbanPersona(String ibanPersona) { this.ibanPersona = ibanPersona; }

    public String getEmailPersona() { return emailPersona; }
    public void setEmailPersona(String emailPersona) { this.emailPersona = emailPersona; }

    public String getPassPersona() { return passPersona; }
    public void setPassPersona(String passPersona) { this.passPersona = passPersona; }

    public String getConfirmPassPersona() { return confirmPassPersona; }
    public void setConfirmPassPersona(String confirmPassPersona) { this.confirmPassPersona = confirmPassPersona; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

   // public Long getEmpresaId() { return empresaId; }
    //public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
}
