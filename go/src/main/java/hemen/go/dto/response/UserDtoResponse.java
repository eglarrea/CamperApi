package hemen.go.dto.response;

import java.time.LocalDate;

public class UserDtoResponse {

    private Long id;
    private String nombrePersona;
    private String apellidosPersona;
    private LocalDate fecNacimientoPersona;
    private String dniPersona;
    private String ibanPersona;
    private String emailPersona;
    private boolean isAdmin;
    private String empresaNombre;

    // Constructor vacío
    public UserDtoResponse() {}

    // Constructor con parámetros
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
