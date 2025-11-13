package hemen.go.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
 * Esta clase encapsula la lógica de autenticación de usuarios y la
 * generación de tokens JWT. Se integra con Spring Security para validar
 * credenciales y con JwtUtil para emitir tokens seguros.
 *
 * Funcionalidades principales:
 *  - Validar credenciales de usuario mediante AuthenticationManager.
 *  - Cargar detalles del usuario con UserDetailsService.
 *  - Generar un token JWT válido para el usuario autenticado.
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;
    
    private final PasswordEncoder passwordEncoder;
    
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param authenticationManager componente de Spring Security para autenticar usuarios.
     * @param jwtUtil utilidad para generar y validar tokens JWT.
     * @param userDetailsService servicio para cargar detalles de usuario.
     */
    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService,PasswordEncoder passwordEncoder,UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository=usuarioRepository;
    }

    /**
     * Autentica un usuario y genera un token JWT.
     *
     * Flujo:
     *  1. Valida las credenciales (email y contraseña) usando AuthenticationManager.
     *  2. Carga los detalles del usuario (roles, permisos) con UserDetailsService.
     *  3. Genera un token JWT firmado con la clave secreta.
     *
     * @param email correo electrónico del usuario.
     * @param password contraseña del usuario.
     * @return token JWT válido para el usuario autenticado.
     */
    public String authenticate(String email, String password) {
        // 1. Validar credenciales con Spring Security
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        // 2. Cargar detalles del usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // 3. Generar token JWT
        return jwtUtil.generateToken(userDetails);
    }

    
    
    public void register(RegisterRequest request) {
    	 // 1. Validar que las contraseñas coincidan
        if (!request.getPassPersona().equals(request.getConfirmPassPersona())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        // 2. Encriptar la contraseña
        String encodedPassword = passwordEncoder.encode(request.getPassPersona());

        // 3. Crear entidad User
        Usuario user = new Usuario();
        user.setNombre_persona(request.getNombrePersona());
        user.setApellidos_persona(request.getApellidosPersona());
        user.setFec_nacimiento_persona(request.getFecNacimientoPersona());
        user.setDni_persona(request.getDniPersona());
        user.setIban_persona(request.getIbanPersona());
        user.setEmailPersona(request.getEmailPersona());
        user.setPass_persona(encodedPassword);
        user.setIs_admin(request.isAdmin());

        // 4. Guardar en la base de datos
        usuarioRepository.save(user);
    }
    
}
