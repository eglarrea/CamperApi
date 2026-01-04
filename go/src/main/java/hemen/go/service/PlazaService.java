package hemen.go.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hemen.go.dto.request.PlazaRequest;
import hemen.go.dto.response.PlazaResponse;
import hemen.go.entity.Parking;
import hemen.go.entity.Plaza;
import hemen.go.entity.Usuario;
import hemen.go.enums.EstadoPlaza;
import hemen.go.repository.ParkingRepository;
import hemen.go.repository.PlazaRepository;
import hemen.go.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Servicio de gestión de plazas de parking para la aplicación Hemengo.
 *
 * Esta clase centraliza la lógica de negocio relacionada con la administración de plazas
 * dentro de los parkings de una empresa. Permite consultar, crear y actualizar plazas.
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Consultar plazas asociadas a una empresa y parking concreto.</li>
 *   <li>Registrar nuevas plazas en un parking existente.</li>
 *   <li>Actualizar información de plazas existentes.</li>
 * </ul>
 *
 * <p>Excepciones:</p>
 * <ul>
 *   <li>{@link UsernameNotFoundException} si el usuario no existe o no tiene empresa asociada.</li>
 *   <li>{@link EntityNotFoundException} si el parking o la plaza no existen.</li>
 *   <li>{@link IllegalArgumentException} si la plaza no pertenece al parking indicado.</li>
 * </ul>
 */
@Service
public class PlazaService {
    private static final Logger logger = LoggerFactory.getLogger(PlazaService.class);

    private final ParkingRepository parkingRepository;
    private final PlazaRepository plazaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MessageSource messageSource;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param parkingRepository repositorio de parkings.
     * @param plazaRepository repositorio de plazas.
     * @param usuarioRepository repositorio de usuarios.
     * @param messageSource fuente de mensajes internacionalizados.
     */
    public PlazaService(ParkingRepository parkingRepository, PlazaRepository plazaRepository,
                        UsuarioRepository usuarioRepository, MessageSource messageSource) {
        this.parkingRepository = parkingRepository;
        this.plazaRepository = plazaRepository;
        this.usuarioRepository = usuarioRepository;
        this.messageSource = messageSource;
    }

    /**
     * Obtiene una plaza específica asociada a la empresa de un usuario.
     *
     * <p>Flujo:</p>
     * <ol>
     *   <li>Valida que el usuario exista y tenga empresa asociada.</li>
     *   <li>Busca la plaza por su ID y la empresa del usuario.</li>
     *   <li>Devuelve la plaza en formato {@link PlazaResponse}.</li>
     * </ol>
     *
     * @param email correo electrónico del usuario.
     * @param idPlaza identificador de la plaza.
     * @return objeto {@link PlazaResponse} con la información de la plaza.
     * @throws UsernameNotFoundException si el usuario no existe o no tiene empresa asociada.
     */
    public PlazaResponse findByCompanyAndPlazaId(String email, Long idPlaza) {
        Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
        if (user.getEmpresa() == null || user.getEmpresa().getId() == null) {
            logger.error("El usuario email " + email + " no tiene empresa asociada");
            throw new UsernameNotFoundException(
                    messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale()));
        }
        return plazaRepository.findByIdAndParking_Empresa_Id(user.getEmpresa().getId().longValue(), idPlaza)
                .map(PlazaResponse::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
    }

    /**
     * Añade una nueva plaza a un parking existente.
     *
     * <p>Flujo:</p>
     * <ol>
     *   <li>Valida que el usuario exista y tenga empresa asociada.</li>
     *   <li>Busca el parking por su ID.</li>
     *   <li>Crea una nueva entidad {@link Plaza} con los datos del request.</li>
     *   <li>Guarda la plaza en la base de datos.</li>
     *   <li>Devuelve la plaza en formato {@link PlazaResponse}.</li>
     * </ol>
     *
     * @param email correo electrónico del usuario.
     * @param parkingId identificador del parking.
     * @param request objeto {@link PlazaRequest} con los datos de la plaza.
     * @return objeto {@link PlazaResponse} con la plaza creada.
     * @throws UsernameNotFoundException si el usuario no existe o no tiene empresa asociada.
     * @throws EntityNotFoundException si el parking no existe.
     */
    public PlazaResponse addPlazaToParking(String email, Long parkingId, PlazaRequest request) {
        Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
        if (user.getEmpresa() == null || user.getEmpresa().getId() == null) {
            logger.error("El usuario email " + email + " no tiene empresa asociada");
            throw new UsernameNotFoundException(
                    messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale()));
        }

        Parking parking = parkingRepository.findById(parkingId)
                .orElseThrow(() -> new EntityNotFoundException("Parking no encontrado"));

        Plaza plaza = new Plaza();
        plaza.setNombre(request.getNombre());
        plaza.setEsVip(request.isEsVip());
        plaza.setTieneElectricidad(request.isTieneElectricidad());
        plaza.setEstado(EstadoPlaza.fromCodigo(request.getEstado()));
        plaza.setPrecio(request.getPrecio());
        plaza.setParking(parking);

        Plaza saved = plazaRepository.save(plaza);
        return new PlazaResponse(saved);
    }

    /**
     * Actualiza los datos de una plaza existente.
     *
     * <p>Flujo:</p>
     * <ol>
     *   <li>Valida que el usuario exista y tenga empresa asociada.</li>
     *   <li>Busca el parking y la plaza por sus IDs.</li>
     *   <li>Verifica que la plaza pertenezca al parking indicado.</li>
     *   <li>Actualiza los datos de la plaza con la información del request.</li>
     *   <li>Guarda los cambios en la base de datos.</li>
     *   <li>Devuelve la plaza actualizada en formato {@link PlazaResponse}.</li>
     * </ol>
     *
     * @param email correo electrónico del usuario.
     * @param parkingId identificador del parking.
     * @param plazaId identificador de la plaza.
     * @param request objeto {@link PlazaRequest} con los nuevos datos.
     * @return objeto {@link PlazaResponse} con la plaza actualizada.
     * @throws UsernameNotFoundException si el usuario no existe o no tiene empresa asociada.
     * @throws EntityNotFoundException si el parking o la plaza no existen.
     * @throws IllegalArgumentException si la plaza no pertenece al parking indicado.
     */
    public PlazaResponse updatePlaza(String email, Long parkingId, Long plazaId, PlazaRequest request) {
        Usuario user = usuarioRepository.findByEmailPersona(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

        if (user.getEmpresa() == null || user.getEmpresa().getId() == null) {
            logger.error("El usuario con email " + email + " no tiene empresa asociada");
            throw new UsernameNotFoundException(
                    messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale()));
        }

        Parking parking = parkingRepository.findById(parkingId)
                .orElseThrow(() -> new EntityNotFoundException("Parking no encontrado"));

        Plaza plaza = plazaRepository.findById(plazaId)
                .orElseThrow(() -> new EntityNotFoundException("Plaza no encontrada"));

        if (!plaza.getParking().getId().equals(parking.getId())) {
            throw new IllegalArgumentException("La plaza no pertenece al parking especificado");
        }
        
        if (request.getNombre() != null) {
            plaza.setNombre(request.getNombre());
        }

        if (request.isEsVip() != null) { // si es Boolean en vez de boolean
            plaza.setEsVip(request.isEsVip());
        }

        if (request.isTieneElectricidad() != null) {
            plaza.setTieneElectricidad(request.isTieneElectricidad());
        }

        if (request.getEstado() != null) {
            plaza.setEstado(EstadoPlaza.fromCodigo(request.getEstado()));
        }

        if (request.getPrecio() != null) {
            plaza.setPrecio(request.getPrecio());
        }

        Plaza updated = plazaRepository.save(plaza);
        return new PlazaResponse(updated);
    }
}
