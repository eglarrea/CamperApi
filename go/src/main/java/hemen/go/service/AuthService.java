package hemen.go.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

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

    /** Administrador de autenticación de Spring Security */
    private final AuthenticationManager authenticationManager;

    /** Utilidad para generar y validar tokens JWT */
    private final JwtUtil jwtUtil;

    /** Servicio para cargar detalles de usuario (roles, credenciales) */
    private final UserDetailsService userDetailsService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param authenticationManager componente de Spring Security para autenticar usuarios.
     * @param jwtUtil utilidad para generar y validar tokens JWT.
     * @param userDetailsService servicio para cargar detalles de usuario.
     */
    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
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

    /*
    // Método para registrar nuevos usuarios (actualmente comentado).
    public void register(RegisterRequest request) {
        // Lógica para guardar un nuevo usuario en la BD.
        // Encriptar contraseña con BCryptPasswordEncoder antes de persistir.
    }
    */
}
