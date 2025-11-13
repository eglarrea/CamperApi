package hemen.go.dto.request;

/**
 * DTO (Data Transfer Object) para la petición de login.
 *
 * Esta clase se utiliza para recibir los datos enviados por el cliente
 * al realizar una solicitud de autenticación (login).
 *
 * Campos principales:
 *  - email: dirección de correo electrónico del usuario.
 *  - password: contraseña asociada al usuario.
 *
 * Características:
 *  - Incluye un constructor vacío, necesario para que frameworks como
 *    Spring Boot puedan deserializar automáticamente el JSON recibido
 *    en el cuerpo de la petición.
 *  - Incluye un constructor con parámetros para facilitar la creación
 *    manual del objeto en pruebas o instanciaciones directas.
 *  - Proporciona getters y setters para acceder y modificar los campos.
 */
public class LoginRequest {

    /** Dirección de correo electrónico del usuario */
    private String email;

    /** Contraseña del usuario */
    private String password;

    /**
     * Constructor vacío.
     * Necesario para la deserialización automática de JSON a objeto
     * por parte de Spring Boot y librerías como Jackson.
     */
    public LoginRequest() {}

    /**
     * Constructor con parámetros.
     *
     * @param email dirección de correo electrónico del usuario.
     * @param password contraseña del usuario.
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters y setters

    /** @return el email del usuario */
    public String getEmail() {
        return email;
    }

    /** @param email establece el email del usuario */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return la contraseña del usuario */
    public String getPassword() {
        return password;
    }

    /** @param password establece la contraseña del usuario */
    public void setPassword(String password) {
        this.password = password;
    }
}
