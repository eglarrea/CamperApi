package hemen.go.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hemen.go.controller.secure.ReservaController;
import hemen.go.dto.request.RegisterRequest;
import hemen.go.dto.response.ReservaResponse;
import hemen.go.dto.response.UserDtoResponse;
import hemen.go.entity.Usuario;
import hemen.go.repository.UsuarioRepository;

/**
 * Servicio para la gestión de usuarios en la aplicación Hemengo.
 *
 * <p>
 * Esta clase encapsula la lógica de acceso a datos de usuarios y transforma las
 * entidades de la base de datos en objetos DTO que pueden ser devueltos al
 * cliente.
 * </p>
 *
 * <p>
 * Funcionalidades principales:
 * </p>
 * <ul>
 * <li>Consultar usuarios por su email.</li>
 * <li>Consultar usuarios por su ID.</li>
 * <li>Obtener datos del usuario autenticado.</li>
 * <li>Actualizar datos de usuario, incluyendo validación de contraseña.</li>
 * <li>Convertir la entidad {@link Usuario} en un objeto
 * {@link UserDtoResponse}.</li>
 * </ul>
 *
 * <p>
 * Las excepciones se gestionan con mensajes internacionalizados mediante
 * {@link MessageSource} y {@link LocaleContextHolder}.
 * </p>
 */
@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	/** Repositorio para acceder a los usuarios en la base de datos. */
	private final UsuarioRepository usuarioRepository;

	/** Codificador de contraseñas para almacenar contraseñas seguras. */
	private final PasswordEncoder passwordEncoder;

	/** Fuente de mensajes para internacionalización (i18n). */
	private final MessageSource messageSource;

	/**
	 * Constructor con inyección de dependencias.
	 *
	 * @param usuarioRepository repositorio para acceder a los usuarios.
	 * @param passwordEncoder   codificador de contraseñas.
	 * @param messageSource     fuente de mensajes para i18n.
	 */
	public UserService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
			MessageSource messageSource) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.messageSource = messageSource;
	}

	/**
	 * Busca un usuario por su email y devuelve un DTO con sus datos.
	 *
	 * <p>
	 * Flujo:
	 * </p>
	 * <ol>
	 * <li>Consulta el repositorio usando el email.</li>
	 * <li>Si no existe, lanza {@link UsernameNotFoundException} con mensaje
	 * traducido.</li>
	 * <li>Si existe, construye un objeto {@link UserDtoResponse} con:
	 * <ul>
	 * <li>ID, nombre, apellidos, fecha de nacimiento.</li>
	 * <li>DNI, IBAN, email.</li>
	 * <li>Rol administrador (isAdmin).</li>
	 * <li>Nombre de la empresa asociada (si existe).</li>
	 * </ul>
	 * </li>
	 * </ol>
	 *
	 * @param email correo electrónico del usuario.
	 * @return objeto UserDtoResponse con los datos del usuario.
	 * @throws UsernameNotFoundException si el usuario no existe.
	 */
	public UserDtoResponse findByEmail(String email) {
		/*
		 * Usuario user = usuarioRepository.findByEmailPersona(email) .orElseThrow(() ->
		 * new UsernameNotFoundException(
		 * messageSource.getMessage("error.usuario.no.existe", null,
		 * LocaleContextHolder.getLocale())));
		 * 
		 * return new UserDtoResponse( user.getId(), user.getNombre_persona(),
		 * user.getApellidos_persona(), user.getFec_nacimiento_persona(),
		 * user.getDni_persona(), user.getIban_persona(), user.getEmailPersona(),
		 * user.is_admin(), user.getEmpresa() != null ?
		 * user.getEmpresa().getNombreEmpresa() : null );
		 */
		return usuarioRepository.findByEmailPersona(email).map(UserDtoResponse::new)
				.orElseThrow(() -> new UsernameNotFoundException(
						messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

	}

	/**
	 * Obtiene los datos de un usuario por su email.
	 *
	 * @param email correo electrónico del usuario.
	 * @return entidad {@link Usuario}.
	 * @throws ResponseStatusException si el usuario no existe (404).
	 */
	public UserDtoResponse getMyData(String email) {
		/*
		 * String mensaje = messageSource.getMessage("error.usuario.no.existe", null,
		 * LocaleContextHolder.getLocale()); return
		 * usuarioRepository.findByEmailPersona(email) .orElseThrow(() -> new
		 * ResponseStatusException(HttpStatus.NOT_FOUND, mensaje));
		 */
		return usuarioRepository.findByEmailPersona(email).map(UserDtoResponse::new)
				.orElseThrow(() -> new UsernameNotFoundException(
						messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
	}

	/**
	 * Obtiene los datos de un usuario por su ID.
	 *
	 * @param id identificador del usuario.
	 * @return entidad {@link Usuario}.
	 * @throws ResponseStatusException si el usuario no existe (404).
	 */
	public UserDtoResponse getUserById(Long id) {
		// String mensaje = messageSource.getMessage("error.usuario.no.existe", null,
		// LocaleContextHolder.getLocale());
		return usuarioRepository.findById(id).map(UserDtoResponse::new).orElseThrow(() -> new UsernameNotFoundException(
				messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
	}

	/**
	 * Actualiza los datos de un usuario identificado por su email.
	 *
	 * <p>
	 * Flujo:
	 * </p>
	 * <ol>
	 * <li>Busca el usuario en el repositorio.</li>
	 * <li>Si no existe, lanza {@link ResponseStatusException} con mensaje
	 * traducido.</li>
	 * <li>Si se envía nueva contraseña:
	 * <ul>
	 * <li>Valida que coincida con la confirmación.</li>
	 * <li>Codifica la contraseña antes de guardarla.</li>
	 * </ul>
	 * </li>
	 * <li>Actualiza solo los campos no nulos y diferentes al valor actual:
	 * <ul>
	 * <li>Nombre, apellidos, IBAN, fecha de nacimiento.</li>
	 * </ul>
	 * </li>
	 * <li>Guarda el usuario actualizado en la base de datos.</li>
	 * <li>Devuelve un {@link UserDtoResponse} con los datos actualizados.</li>
	 * </ol>
	 *
	 * @param email       correo electrónico del usuario.
	 * @param updatedData objeto {@link RegisterRequest} con los datos actualizados.
	 * @return objeto UserDtoResponse con los datos actualizados.
	 * @throws ResponseStatusException  si el usuario no existe.
	 * @throws IllegalArgumentException si la confirmación de contraseña no
	 *                                  coincide.
	 */
	public UserDtoResponse updateUserData(String email, RegisterRequest updatedData) {
		Usuario usuario = usuarioRepository.findByEmailPersona(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

		// Validar y actualizar contraseña
		if (updatedData.getPassPersona() != null && !updatedData.getPassPersona().isBlank()
				&& !updatedData.getPassPersona().equals(usuario.getPass_persona())) {
			if (!updatedData.getPassPersona().equals(updatedData.getConfirmPassPersona())) {
				String mensaje = messageSource.getMessage("user.password.confirm", null,
						LocaleContextHolder.getLocale());
				throw new IllegalArgumentException(mensaje);
			}
			String encodedPassword = passwordEncoder.encode(updatedData.getPassPersona());
			usuario.setPass_persona(encodedPassword);
		}

		// Actualizar campos básicos si han cambiado
		if (updatedData.getNombrePersona() != null
				&& !updatedData.getNombrePersona().equals(usuario.getNombre_persona())) {
			usuario.setNombre_persona(updatedData.getNombrePersona());
		}
		if (updatedData.getIbanPersona() != null && !updatedData.getIbanPersona().equals(usuario.getIban_persona())) {
			usuario.setIban_persona(updatedData.getIbanPersona());
		}
		if (updatedData.getApellidosPersona() != null
				&& !updatedData.getApellidosPersona().equals(usuario.getApellidos_persona())) {
			usuario.setApellidos_persona(updatedData.getApellidosPersona());
		}
		if (updatedData.getFecNacimientoPersona() != null
				&& !updatedData.getFecNacimientoPersona().equals(usuario.getFec_nacimiento_persona())) {
			usuario.setFec_nacimiento_persona(updatedData.getFecNacimientoPersona());
		}

		Usuario usua = usuarioRepository.save(usuario);

		return new UserDtoResponse(usua.getId(), usua.getNombre_persona(), usua.getApellidos_persona(),
				usua.getFec_nacimiento_persona(), usua.getDni_persona(), usua.getIban_persona(), usua.getEmailPersona(),
				usua.is_admin(), usua.getEmpresa() != null ? usua.getEmpresa().getNombreEmpresa() : null);
	}

	public List<UserDtoResponse> findByCompanyId(String email) {
		Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
				messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
		if (null == user.getEmpresa() || null == user.getEmpresa().getId()) {
			logger.error("El usuario email" + email + " no tiene empresa asociada");
			throw new UsernameNotFoundException(
					messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale()));
		}
		List<Usuario> usuarios = usuarioRepository.findByEmpresa_Id(user.getEmpresa().getId().longValue());
		return usuarios.stream().map(UserDtoResponse::new).toList();
	}
}
