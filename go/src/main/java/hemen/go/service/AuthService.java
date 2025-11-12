package hemen.go.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import hemen.go.security.JwtUtil;

@Service
public class AuthService {

	 private final AuthenticationManager authenticationManager;
	    private final JwtUtil jwtUtil;
	    private final UserDetailsService userDetailsService;

	    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
	        this.authenticationManager = authenticationManager;
	        this.jwtUtil = jwtUtil;
	        this.userDetailsService = userDetailsService;
	    }

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

	    /*public void register(RegisterRequest request) {
	        // lógica para guardar un nuevo usuario en la BD
	        // encriptar contraseña con BCryptPasswordEncoder
	    }*/
}
