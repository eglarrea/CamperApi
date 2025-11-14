package hemen.go.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de seguridad JWT para Spring Security.
 *
 * Este filtro se ejecuta una vez por cada petición (extiende OncePerRequestFilter)
 * y se encarga de:
 *  - Extraer el token JWT de la cabecera "Authorization".
 *  - Validar el token y obtener el nombre de usuario.
 *  - Cargar los detalles del usuario desde UserDetailsService.
 *  - Crear una autenticación válida en el contexto de seguridad de Spring.
 *
 * De esta forma, cualquier petición que incluya un JWT válido quedará autenticada
 * en el sistema y podrá acceder a recursos protegidos.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    /** Utilidad para trabajar con JWT (generar, validar, extraer claims) */
    private final JwtUtil jwtUtil;

    /** Servicio para cargar los detalles del usuario (roles, credenciales) */
    private final UserDetailsService userDetailsService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param jwtUtil utilidad para manejar tokens JWT.
     * @param userDetailsService servicio para cargar usuarios.
     */
    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método principal del filtro.
     *
     * Flujo:
     *  1. Obtiene la cabecera "Authorization" de la petición.
     *  2. Si empieza por "Bearer ", extrae el token JWT.
     *  3. Usa JwtUtil para obtener el nombre de usuario del token.
     *  4. Si el usuario no está autenticado en el contexto:
     *      - Carga sus detalles con UserDetailsService.
     *      - Valida el token contra esos detalles.
     *      - Crea un objeto UsernamePasswordAuthenticationToken con roles y permisos.
     *      - Lo establece en el SecurityContextHolder (autenticación activa).
     *  5. Continúa la cadena de filtros (filterChain.doFilter).
     *
     * @param request petición HTTP entrante.
     * @param response respuesta HTTP saliente.
     * @param filterChain cadena de filtros de Spring Security.
     * @throws ServletException en caso de error de servlet.
     * @throws java.io.IOException en caso de error de I/O.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, java.io.IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Extraer token si existe y empieza por "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        // Validar token y establecer autenticación en el contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continuar con el resto de filtros
        filterChain.doFilter(request, response);
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/swagger-ui")
            || path.startsWith("/v3/api-docs")
            || path.startsWith("/swagger-resources")
            || path.startsWith("/webjars");
    }
}
