package hemen.go.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hemen.go.dto.response.UserDtoResponse;
import hemen.go.entity.User;
import hemen.go.repository.UsuarioRepository;

@Service
public class UserService {

	private final UsuarioRepository usuarioRepository;

    public UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    
    public UserDtoResponse findByEmail(String email) {
        User user = usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new UserDtoResponse(user.getId(),
        	    user.getNombre_persona(),
        	    user.getApellidos_persona(),
        	    user.getFec_nacimiento_persona(),
        	    user.getDni_persona(),
        	    user.getIban_persona(),
        	    user.getEmailPersona(),
        	    user.isIs_admin(),
        	    user.getEmpresa() != null ? user.getEmpresa().getNombreEmpresa() : null);
    }
}