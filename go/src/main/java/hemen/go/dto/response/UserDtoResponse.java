package hemen.go.dto.response;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para la respuesta de usuario.
 *
 * Esta clase se utiliza para enviar al cliente los datos de un usuario
 * en operaciones como login, consultas de perfil o listados.
 *
 * Campos principales:
 *  - id: identificador único del usuario.
 *  - nombrePersona: nombre del usuario.
 *  - apellidosPersona: apellidos del usuario.
 *  - fecNacimientoPersona: fecha de nacimiento del usuario.
 *  - dniPersona: documento nacional de identidad.
 *  - ibanPersona: número de cuenta bancaria en formato IBAN.
 *  - emailPersona: correo electrónico del usuario.
 *  - isAdmin: indica si el usuario tiene rol de administrador.
 *  - empresaNombre: nombre de la empresa asociada al usuario.
 *
 * Características:
 *  - Incluye un constructor vacío, necesario para la deserialización JSON
 *    y compatibilidad con frameworks como Jackson.
 *  - Incluye un constructor con parámetros para facilitar la creación
 *    del objeto en servicios o pruebas.
 *  - Proporciona getters y setters para acceder y modificar los campos.
 *
 * Ejemplo de respuesta JSON:
 * {
 *   "id": 1,
 *   "nombrePersona": "Jon",
 *   "apellidosPersona": "Doe",
 *   "fecNacimientoPersona": "1990-05-12",
 *   "dniPersona": "12345678A",
 *   "ibanPersona": "ES1234567890123456789012",
 *   "emailPersona": "jon.doe@example.com",
 *   "isAdmin": false,
 *   "empresaNombre": "Hemengo S.L."
 * }
 */
public class UserDtoResponse {

    /** Identificador único del usuario */
    private Long id;

    /** Nombre del usuario */
    private String nombrePersona;

    /** Apellidos del usuario */
    private String apellidosPersona;

    /** Fecha de nacimiento del usuario */
    private LocalDate fecNacimientoPersona;

    /** Documento nacional de identidad */
    private String dniPersona;

    /** Número de cuenta bancaria en formato IBAN */
    private String ibanPersona;

    /** Correo electrónico del usuario */
    private String emailPersona;

    /** Rol de administrador */
    private boolean isAdmin;

    /** Nombre de la empresa asociada */
    private String empresaNombre;

    /** Constructor vacío (necesario para deserialización JSON) */
    public UserDtoResponse() {}

    /**
     * Constructor con parámetros.
     *
     * @param id identificador único del usuario
     * @param nombrePersona nombre del usuario
     * @param apellidosPersona apellidos del usuario
     * @param fecNacimientoPersona fecha de nacimiento
     * @param dniPersona DNI del usuario
     * @param ibanPersona IBAN del usuario
     * @param emailPersona correo electrónico
     * @param isAdmin rol administrador
     * @param empresaNombre nombre de la empresa asociada
     */
    public UserDtoResponse(Long id, String nombrePersona, String apellidosPersona,
                           LocalDate fecNacimientoPersona, String dniPersona,
                           String ibanPersona, String emailPersona,
                           boolean isAdmin, String empresaNombre) {
        this.id = id;
        this.nombrePersona = nombrePersona;
        this.apellidosPersona = apellidosPersona;
        this.fecNacimientoPersona = fecNacimientoPersona;
        this.dniPersona = dniPersona;
        this.ibanPersona = ibanPersona;
        this.emailPersona = emailPersona;
        this.isAdmin = isAdmin;
        this.empresaNombre = empresaNombre;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

    public String getEmpresaNombre() { return empresaNombre; }
    public void setEmpresaNombre(String empresaNombre) { this.empresaNombre = empresaNombre; }
}
