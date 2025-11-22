package hemen.go.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * Configuración principal de seguridad para la aplicación Hemengo.
 *
 * Esta clase define cómo se gestionan la autenticación y autorización
 * en la aplicación usando Spring Security y JWT.
 *
 * Funcionalidades principales:
 *  - Deshabilitar CSRF (no necesario en APIs REST).
 *  - Definir qué endpoints son públicos y cuáles requieren autenticación.
 *  - Configurar la política de sesiones como STATELESS (sin sesiones en servidor).
 *  - Registrar el filtro JWT antes del filtro estándar de autenticación.
 *  - Proporcionar un AuthenticationManager para manejar autenticaciones.
 *  - Definir un PasswordEncoder seguro (BCrypt).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /** Filtro JWT que valida tokens en cada petición */
    private final JwtFilter jwtFilter;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param jwtFilter filtro JWT para validar tokens en las peticiones.
     */
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Define la cadena de filtros de seguridad.
     *
     * Configuración:
     *  - CSRF deshabilitado (no necesario en APIs REST).
     *  - Endpoints bajo "/api/public/**" accesibles sin autenticación.
     *  - Endpoints bajo "/api/secure/**" requieren autenticación.
     *  - Sesiones configuradas como STATELESS (cada petición debe incluir JWT).
     *  - Se añade el filtro JWT antes del filtro estándar UsernamePasswordAuthenticationFilter.
     *
     * @param http objeto HttpSecurity para configurar seguridad.
     * @return SecurityFilterChain configurado.
     * @throws Exception en caso de error de configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {}) // habilita CORS con la configuración global
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            		 .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            		 .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
            		 .requestMatchers("/api/public/**").permitAll()
            		 .requestMatchers("/api/secure/**").authenticated()
            		 .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:4200","https://camper-jhyc.onrender.com","https://prueba-pyxw.onrender.com"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    /**
     * Bean para obtener el AuthenticationManager.
     *
     * - Se usa para autenticar usuarios en el sistema.
     * - Se obtiene desde AuthenticationConfiguration.
     *
     * @param authenticationConfiguration configuración de autenticación.
     * @return AuthenticationManager configurado.
     * @throws Exception en caso de error.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean para codificar contraseñas.
     *
     * - Se usa BCryptPasswordEncoder, un algoritmo seguro y recomendado.
     * - Garantiza que las contraseñas se almacenen de forma segura en la base de datos.
     *
     * @return PasswordEncoder basado en BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
