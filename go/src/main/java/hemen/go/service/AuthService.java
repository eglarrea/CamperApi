package hemen.go.service;

import java.time.LocalDate;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hemen.go.dto.request.RegisterRequest;
import hemen.go.entity.Usuario;
import hemen.go.repository.UsuarioRepository;
import hemen.go.security.JwtUtil;

/**
 * Servicio de autenticación para la aplicación Hemengo.
 *
 * Esta clase centraliza la lógica relacionada con la autenticación y el registro
 * de usuarios en el sistema. Se integra con Spring Security para validar credenciales
 * y con {@link JwtUtil} para generar tokens JWT seguros.
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Autenticar usuarios mediante {@link AuthenticationManager}.</li>
 *   <li>Cargar detalles del usuario con {@link UserDetailsService}.</li>
 *   <li>Generar tokens JWT válidos para sesiones autenticadas.</li>
 *   <li>Registrar nuevos usuarios en la base de datos con validaciones de negocio.</li>
 * </ul>
 *
 * <p>Excepciones:</p>
 * <ul>
 *   <li>{@link IllegalArgumentException} si las contraseñas no coinciden o si la fecha de nacimiento no es válida.</li>
 * </ul>
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final MessageSource messageSource;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param authenticationManager componente de Spring Security para autenticar usuarios.
     * @param jwtUtil utilidad para generar y validar tokens JWT.
     * @param userDetailsService servicio para cargar detalles de usuario.
     * @param passwordEncoder codificador de contraseñas para almacenamiento seguro.
     * @param usuarioRepository repositorio para persistir entidades {@link Usuario}.
     * @param messageSource fuente de mensajes internacionalizados para errores y validaciones.
     */
    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, 
                       UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                       UsuarioRepository usuarioRepository, MessageSource messageSource) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.messageSource = messageSource;
    }

    /**
     * Autentica un usuario y genera un token JWT.
     *
     * <p>Flujo:</p>
     * <ol>
     *   <li>Valida las credenciales (email y contraseña) usando {@link AuthenticationManager}.</li>
     *   <li>Carga los detalles del usuario (roles, permisos) con {@link UserDetailsService}.</li>
     *   <li>Genera un token JWT firmado con la clave secreta.</li>
     * </ol>
     *
     * @param email correo electrónico del usuario.
     * @param password contraseña del usuario.
     * @return token JWT válido para el usuario autenticado.
     * @throws org.springframework.security.core.AuthenticationException si las credenciales son inválidas.
     */
    public String authenticate(String email, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return jwtUtil.generateToken(userDetails);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * <p>Flujo:</p>
     * <ol>
     *   <li>Valida que las contraseñas coincidan.</li>
     *   <li>Verifica que la fecha de nacimiento corresponda a un usuario mayor de edad (≥ 18 años).</li>
     *   <li>Encripta la contraseña con {@link PasswordEncoder}.</li>
     *   <li>Crea una nueva entidad {@link Usuario} con los datos proporcionados.</li>
     *   <li>Persiste el usuario en la base de datos mediante {@link UsuarioRepository}.</li>
     * </ol>
     *
     * @param request objeto {@link RegisterRequest} con los datos del usuario a registrar.
     * @throws IllegalArgumentException si las contraseñas no coinciden o si la fecha de nacimiento es inválida.
     */
    public void register(RegisterRequest request) {
        if (!request.getPassPersona().equals(request.getConfirmPassPersona())) {
            String mensaje = messageSource.getMessage(
                "error.password.usuario", null, LocaleContextHolder.getLocale());
            throw new IllegalArgumentException(mensaje);
        }
        if (request.getFecNacimientoPersona() != null &&
            request.getFecNacimientoPersona().plusYears(18).isAfter(LocalDate.now())) {
            String mensaje = messageSource.getMessage(
                "user.birthdate.past", null, LocaleContextHolder.getLocale());
            throw new IllegalArgumentException(mensaje);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassPersona());

        Usuario user = new Usuario();
        user.setNombre_persona(request.getNombrePersona());
        user.setApellidos_persona(request.getApellidosPersona());
        user.setFec_nacimiento_persona(request.getFecNacimientoPersona());
        user.setDni_persona(request.getDniPersona());
        user.setIban_persona(request.getIbanPersona());
        user.setEmailPersona(request.getEmailPersona());
        user.setPass_persona(encodedPassword);
        user.setIs_admin(false);

        usuarioRepository.save(user);
    }
}
