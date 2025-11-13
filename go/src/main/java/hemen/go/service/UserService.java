package hemen.go.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hemen.go.dto.response.UserDtoResponse;
import hemen.go.entity.User;
import hemen.go.repository.UsuarioRepository;

/**
 * Servicio para la gestión de usuarios en la aplicación Hemengo.
 *
 * Esta clase encapsula la lógica de acceso a datos de usuarios y
 * transforma las entidades de la base de datos en objetos DTO
 * que pueden ser devueltos al cliente.
 *
 * Funcionalidades principales:
 *  - Consultar usuarios por su email.
 *  - Lanzar excepción si el usuario no existe.
 *  - Convertir la entidad User en un objeto UserDtoResponse.
 */
@Service
public class UserService {

    /** Repositorio para acceder a los usuarios en la base de datos */
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioRepository repositorio para acceder a los usuarios.
     */
    public UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Busca un usuario por su email y devuelve un DTO con sus datos.
     *
     * Flujo:
     *  1. Consulta el repositorio usando el email.
     *  2. Si no existe, lanza UsernameNotFoundException.
     *  3. Si existe, construye un objeto UserDtoResponse con:
     *      - id, nombre, apellidos, fecha de nacimiento.
     *      - DNI, IBAN, email.
     *      - Rol administrador (isAdmin).
     *      - Nombre de la empresa asociada (si existe).
     *
     * @param email correo electrónico del usuario.
     * @return objeto UserDtoResponse con los datos del usuario.
     * @throws UsernameNotFoundException si el usuario no existe.
     */
    public UserDtoResponse findByEmail(String email) {
        User user = usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new UserDtoResponse(
                user.getId(),
                user.getNombre_persona(),
                user.getApellidos_persona(),
                user.getFec_nacimiento_persona(),
                user.getDni_persona(),
                user.getIban_persona(),
                user.getEmailPersona(),
                user.isIs_admin(),
                user.getEmpresa() != null ? user.getEmpresa().getNombreEmpresa() : null
        );
    }
}
