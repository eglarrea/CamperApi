package hemen.go.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hemen.go.dto.request.RegisterRequest;
import hemen.go.dto.response.UserDtoResponse;
import hemen.go.entity.Usuario;
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
    
    private final PasswordEncoder passwordEncoder;
    
    private final MessageSource messageSource;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioRepository repositorio para acceder a los usuarios.
     */
    public UserService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,MessageSource messageSource) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
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
        Usuario user = usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new UsernameNotFoundException( messageSource.getMessage(
                        "error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

        return new UserDtoResponse(
                user.getId(),
                user.getNombre_persona(),
                user.getApellidos_persona(),
                user.getFec_nacimiento_persona(),
                user.getDni_persona(),
                user.getIban_persona(),
                user.getEmailPersona(),
                user.is_admin(),
                user.getEmpresa() != null ? user.getEmpresa().getNombreEmpresa() : null
        );
    }
    
    public Usuario getMyData(String email) {
    	String mensaje = messageSource.getMessage(
                "error.usuario.no.existe", null, LocaleContextHolder.getLocale());
        return usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, mensaje));
    }
    
    public Usuario getUserById(Long id) {
    	String mensaje = messageSource.getMessage(
                "error.usuario.no.existe", null, LocaleContextHolder.getLocale());
    	return usuarioRepository.findById(id)
                 .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, mensaje));
    }
    
    public UserDtoResponse updateUserData(String email, RegisterRequest updatedData) {
    	
        Usuario usuario = usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, messageSource.getMessage(
                        "error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

        // Validar y actualizar contraseña solo si se ha enviado y coincide con confirmación
        if (updatedData.getPassPersona() != null && !updatedData.getPassPersona().isBlank()) {
            if (!updatedData.getPassPersona().equals(updatedData.getConfirmPassPersona())) {
            	
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, messageSource.getMessage(
                        "user.password.confirm", null, LocaleContextHolder.getLocale()));
            }
            String encodedPassword = passwordEncoder.encode(updatedData.getPassPersona());
            usuario.setPass_persona(encodedPassword);
        }
        
        // Actualizar solo si el campo no es nulo y ha cambiado
        if (updatedData.getNombrePersona() != null 
                && !updatedData.getNombrePersona().equals(usuario.getNombre_persona())) {
            usuario.setNombre_persona(updatedData.getNombrePersona());
        }

        if(updatedData.getIbanPersona() != null && !updatedData.getIbanPersona().equals(usuario.getIban_persona())) {
        	usuario.setIban_persona(updatedData.getIbanPersona());
        }
        
        if(updatedData.getApellidosPersona() != null && !updatedData.getApellidosPersona().equals(usuario.getApellidos_persona())) {
        	usuario.setApellidos_persona(updatedData.getApellidosPersona());
        }
        
        if(updatedData.getFecNacimientoPersona() != null && !updatedData.getFecNacimientoPersona().equals(usuario.getFec_nacimiento_persona())) {
        	usuario.setFec_nacimiento_persona(updatedData.getFecNacimientoPersona());
        }

        Usuario usua = usuarioRepository.save(usuario);

        return new UserDtoResponse(usua.getId(), usua.getNombre_persona(),	usua.getApellidos_persona(),
        		usua.getFec_nacimiento_persona(), usua.getDni_persona(), usua.getIban_persona(),
        		usua.getEmailPersona(),	usua.is_admin(),
        		usua.getEmpresa() != null ? usua.getEmpresa().getNombreEmpresa() : null
        );
    }
}
