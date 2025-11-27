package hemen.go.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hemen.go.dto.request.FilterParkingRequest;
import hemen.go.dto.response.ParkingDtoFindResponse;
import hemen.go.dto.response.ParkingDtoResponse;
import hemen.go.entity.Parking;
import hemen.go.entity.Usuario;
import hemen.go.repository.ParkingRepository;
import hemen.go.repository.UsuarioRepository;
import hemen.go.service.specification.ParkingSpecs;
import hemen.go.validator.FechaValidator;

/**
 * Servicio de gestión de parkings para la aplicación Hemengo.
 *
 * Esta clase centraliza la lógica de negocio relacionada con la consulta de parkings,
 * incluyendo búsquedas por empresa, filtros avanzados y listados completos.
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Obtener parkings asociados a una empresa concreta.</li>
 *   <li>Filtrar parkings según criterios de disponibilidad, ubicación y servicios.</li>
 *   <li>Listar todos los parkings registrados en el sistema.</li>
 *   <li>Validar fechas de búsqueda mediante {@link FechaValidator}.</li>
 * </ul>
 *
 * <p>Excepciones:</p>
 * <ul>
 *   <li>{@link UsernameNotFoundException} si el usuario no existe o no tiene empresa asociada.</li>
 *   <li>{@link IllegalArgumentException} si las fechas de búsqueda no son válidas.</li>
 * </ul>
 */
@Service
public class ParkingService {
    private static final Logger logger = LoggerFactory.getLogger(ParkingService.class);

    private final ParkingRepository parkingRepository;
    private final UsuarioRepository usuarioRepository;
    private final FechaValidator fechaValidator;
    private final MessageSource messageSource;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param parkingRepository repositorio de parkings.
     * @param usuarioRepository repositorio de usuarios.
     * @param fechaValidator validador de fechas para búsquedas.
     * @param messageSource fuente de mensajes internacionalizados.
     */
    public ParkingService(ParkingRepository parkingRepository, UsuarioRepository usuarioRepository,
                          FechaValidator fechaValidator, MessageSource messageSource) {
        this.parkingRepository = parkingRepository;
        this.usuarioRepository = usuarioRepository;
        this.fechaValidator = fechaValidator;
        this.messageSource = messageSource;
    }

    /**
     * Obtiene todos los parkings asociados a la empresa de un usuario.
     *
     * <p>Flujo:</p>
     * <ol>
     *   <li>Busca el usuario por su email.</li>
     *   <li>Verifica que el usuario tenga una empresa asociada.</li>
     *   <li>Obtiene los parkings vinculados a la empresa.</li>
     *   <li>Transforma las entidades en DTOs de respuesta.</li>
     * </ol>
     *
     * @param email correo electrónico del usuario.
     * @return lista de {@link ParkingDtoResponse} con los parkings de la empresa.
     * @throws UsernameNotFoundException si el usuario no existe o no tiene empresa asociada.
     */
    public List<ParkingDtoResponse> findByCompanyId(String email) {
        Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

        if (user.getEmpresa() == null || user.getEmpresa().getId() == null) {
            logger.error("El usuario con email " + email + " no tiene empresa asociada");
            throw new UsernameNotFoundException(
                    messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale()));
        }

        List<Parking> parking = parkingRepository.findByEmpresa_Id(user.getEmpresa().getId().longValue());
        return parking.stream().map(ParkingDtoResponse::new).toList();
    }

    /**
     * Busca parkings aplicando filtros avanzados.
     *
     * <p>Flujo:</p>
     * <ol>
     *   <li>Valida las fechas de búsqueda mediante {@link FechaValidator}.</li>
     *   <li>Construye una {@link Specification} con los criterios de búsqueda:
     *       provincia, municipio, disponibilidad, electricidad, aguas residuales, plazas VIP.</li>
     *   <li>Ejecuta la consulta en la base de datos.</li>
     *   <li>Transforma los resultados en {@link ParkingDtoFindResponse} incluyendo fechas solicitadas.</li>
     * </ol>
     *
     * @param request objeto {@link FilterParkingRequest} con los criterios de búsqueda.
     * @return lista de {@link ParkingDtoFindResponse} con los parkings filtrados.
     * @throws IllegalArgumentException si las fechas son inválidas.
     */
    public List<ParkingDtoFindResponse> findParkings(FilterParkingRequest request) {
        fechaValidator.validarFechas(request.getFechaDesde(), request.getFechaHasta());

        List<Parking> parkings = parkingRepository.findAll(
                Specification.where(ParkingSpecs.porProvincia(request.getProvincia()))
                        .and(ParkingSpecs.porMunicipio(request.getLocalidad()))
                        .and(ParkingSpecs.porActivo(true))
                        .and(ParkingSpecs.conElectricidad(request.isTomaElectricidad()))
                        .and(ParkingSpecs.conResiduales(request.isLimpiezaAguasResiduales()))
                        .and(ParkingSpecs.conVips(request.isPlazasVip()))
                        .and(ParkingSpecs.conPlazasDisponibles(request.getFechaDesde(), request.getFechaHasta()))
        );

        return parkings.stream()
                .map(p -> new ParkingDtoFindResponse(p, request.getFechaDesde(), request.getFechaHasta()))
                .toList();
    }

    /**
     * Obtiene todos los parkings registrados en el sistema.
     *
     * @return lista de {@link ParkingDtoResponse} con todos los parkings.
     */
    public List<ParkingDtoResponse> findAll() {
        List<Parking> listParking = parkingRepository.findAll();
        return listParking.stream().map(ParkingDtoResponse::new).toList();
    }
}
