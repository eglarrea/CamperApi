package hemen.go.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import hemen.go.dto.request.validate.OnCreate;

/**
 * DTO (Data Transfer Object) para la petición de registro de usuarios.
 *
 * Esta clase se utiliza para recibir y validar los datos enviados por el cliente
 * al realizar una solicitud de registro en la aplicación.
 *
 * Validaciones:
 *  - Se aplican anotaciones de Jakarta Validation (Bean Validation) para garantizar
 *    que los datos cumplan con las reglas de negocio antes de ser procesados.
 *
 * Campos principales:
 *  - nombrePersona: nombre del usuario (obligatorio).
 *  - apellidosPersona: apellidos del usuario (obligatorio).
 *  - fecNacimientoPersona: fecha de nacimiento (obligatoria, debe ser pasada).
 *  - dniPersona: DNI en formato válido (8 dígitos + letra mayúscula).
 *  - ibanPersona: IBAN en formato español (ES + 22 dígitos).
 *  - emailPersona: correo electrónico válido y obligatorio.
 *  - passPersona: contraseña obligatoria.
 *  - confirmPassPersona: confirmación de la contraseña obligatoria.
 *  - isAdmin: indica si el usuario tendrá rol de administrador.
 *
 * Ejemplo de uso:
 *  {
 *    "nombrePersona": "Jon",
 *    "apellidosPersona": "Doe",
 *    "fecNacimientoPersona": "1990-05-12",
 *    "dniPersona": "12345678A",
 *    "ibanPersona": "ES1234567890123456789012",
 *    "emailPersona": "jon.doe@example.com",
 *    "passPersona": "secreta123",
 *    "confirmPassPersona": "secreta123",
 *    "isAdmin": false
 *  }
 */
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

    @NotBlank(message = "{user.password.required}", groups = OnCreate.class)
    private String passPersona;

    @NotBlank(message = "{user.password.confirm}", groups = OnCreate.class)
    private String confirmPassPersona;

    private boolean isAdmin;

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
}
