package hemen.go.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hemen.go.entity.User;
import hemen.go.repository.UsuarioRepository;

/**
 * Implementación personalizada de UserDetailsService para Spring Security.
 *
 * Esta clase se encarga de cargar los datos de un usuario desde la base de datos
 * a partir de su email, y transformarlos en un objeto UserDetails que Spring Security
 * pueda utilizar para la autenticación y autorización.
 *
 * Funcionalidades principales:
 *  - Buscar un usuario en la base de datos por su email.
 *  - Lanzar una excepción si el usuario no existe.
 *  - Construir un objeto UserDetails con la información del usuario.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /** Repositorio para acceder a los usuarios en la base de datos */
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioRepository repositorio para acceder a los usuarios.
     */
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carga un usuario por su email.
     *
     * Flujo:
     *  1. Busca el usuario en la base de datos usando UsuarioRepository.
     *  2. Si no existe, lanza UsernameNotFoundException.
     *  3. Si existe, construye un objeto UserDetails con:
     *      - Username: el email del usuario.
     *      - Password: la contraseña almacenada.
     *      - Roles/authorities: se pueden añadir si el modelo User los define.
     *
     * @param email correo electrónico del usuario.
     * @return objeto UserDetails con la información del usuario.
     * @throws UsernameNotFoundException si el usuario no existe.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmailPersona()) // usamos email como username
                .password(user.getPass_persona())     // contraseña encriptada con BCrypt
                //.roles(user.getRole())              // opcional: añadir roles si están definidos
                .build();
    }
}
