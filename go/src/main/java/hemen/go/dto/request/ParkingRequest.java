package hemen.go.dto.request;

import hemen.go.dto.request.validate.OnCreate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ParkingRequest {

    @NotBlank(message = "{parkingr.name.required}" , groups = OnCreate.class)
    @Size(max = 100 , message = "{parkingr.name.size}")
    private String nombreParking; 

     @Size(max = 255, message = "{parking.provincia.size}") 
    private String provinciaParking; 
    
    @Size(max = 255, message = "{parking.municipio.size}")
    private String municipioParking; 
    
    @Size(max = 255, message = "{parking.web.size}") 
    private String webParking; 
    
    @Size(max = 255, message = "{parking.telefono.size}") 
    private String telefonoParking; 
    
    @Email(message = "{parking.email.invalid}") 
    @Size(max = 255, message = "{parking.email.size}") 
    private String emailParking; 
    
    @Size(max = 255, message = "{parking.personaContacto.size}") 
    private String personaContactoParking;
    
    private Boolean isActivoParking; 
     
    private Boolean tieneElectricidadParking; 

    private Boolean tieneResidualesParking; 

    private Boolean tienePlazasVipParking; 

    public ParkingRequest() {}
    
   
      public String getNombreParking() {
        return nombreParking;
    }

    public void setNombreParking(String nombreParking) {
        this.nombreParking = nombreParking;
    }

    public String getProvinciaParking() {
        return provinciaParking;
    }

    public void setProvinciaParking(String provinciaParking) {
        this.provinciaParking = provinciaParking;
    }

    public String getMunicipioParking() {
        return municipioParking;
    }

    public void setMunicipioParking(String municipioParking) {
        this.municipioParking = municipioParking;
    }

    public Boolean getIsActivoParking() {
        return isActivoParking;
    }

    public void setIsActivoParking(Boolean isActivoParking) {
        this.isActivoParking = isActivoParking;
    }

    public String getWebParking() {
        return webParking;
    }

    public void setWebParking(String webParking) {
        this.webParking = webParking;
    }

    public String getTelefonoParking() {
        return telefonoParking;
    }

    public void setTelefonoParking(String telefonoParking) {
        this.telefonoParking = telefonoParking;
    }

    public String getEmailParking() {
        return emailParking;
    }

    public void setEmailParking(String emailParking) {
        this.emailParking = emailParking;
    }

    public String getPersonaContactoParking() {
        return personaContactoParking;
    }

    public void setPersonaContactoParking(String personaContactoParking) {
        this.personaContactoParking = personaContactoParking;
    }

    public Boolean getTieneElectricidadParking() {
        return tieneElectricidadParking;
    }

    public void setTieneElectricidadParking(Boolean tieneElectricidadParking) {
        this.tieneElectricidadParking = tieneElectricidadParking;
    }

    public Boolean getTieneResidualesParking() {
        return tieneResidualesParking;
    }

    public void setTieneResidualesParking(Boolean tieneResidualesParking) {
        this.tieneResidualesParking = tieneResidualesParking;
    }

    public Boolean getTienePlazasVipParking() {
        return tienePlazasVipParking;
    }

    public void setTienePlazasVipParking(Boolean tienePlazasVipParking) {
        this.tienePlazasVipParking = tienePlazasVipParking;
    }
}

