package hemen.go.dto.response;

/**
 * DTO (Data Transfer Object) para la respuesta de autenticación JWT.
 *
 * Esta clase se utiliza para enviar al cliente la información resultante
 * de un login exitoso:
 *  - El token JWT generado por el sistema de autenticación.
 *  - Los datos del usuario autenticado.
 *
 * Campos principales:
 *  - token: cadena que contiene el JSON Web Token (JWT).
 *  - user: objeto con los datos del usuario (UserDtoResponse).
 *
 * Características:
 *  - Incluye un constructor vacío, necesario para la deserialización y
 *    compatibilidad con frameworks como Jackson.
 *  - Incluye un constructor con parámetros para facilitar la creación
 *    del objeto en el servicio de autenticación.
 *  - Proporciona getters y setters para acceder y modificar los campos.
 *
 * Ejemplo de respuesta JSON:
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "user": {
 *     "id": 1,
 *     "nombre": "Jon",
 *     "email": "jon.doe@example.com"
 *   }
 * }
 */
public class JwtResponse {

    /** Token JWT generado tras la autenticación */
    private String token;

    /** Datos del usuario autenticado */
    private UserDtoResponse user;

    /** Constructor vacío (necesario para deserialización JSON) */
    public JwtResponse() {}

    /**
     * Constructor con parámetros.
     *
     * @param token el JWT generado.
     * @param user datos del usuario autenticado.
     */
    public JwtResponse(String token, UserDtoResponse user) {
        this.token = token;
        this.user = user;
    }

    /** @return el usuario autenticado */
    public UserDtoResponse getUser() {
        return user;
    }

    /** @param user establece el usuario autenticado */
    public void setUser(UserDtoResponse user) {
        this.user = user;
    }

    /** @return el token JWT */
    public String getToken() {
        return token;
    }

    /** @param token establece el token JWT */
    public void setToken(String token) {
        this.token = token;
    }
}
