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
import hemen.go.dto.request.ParkingRequest;
import hemen.go.dto.request.ParkingUpdateRequest;
import hemen.go.dto.response.ParkingDtoFindResponse;
import hemen.go.dto.response.ParkingDtoResponse;
import hemen.go.entity.Empresa;
import hemen.go.entity.Parking;
import hemen.go.entity.Usuario;
import hemen.go.repository.ParkingRepository;
import hemen.go.repository.ReservaRepository;
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
    private final ReservaRepository reservaRepository;
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
                          FechaValidator fechaValidator, ReservaRepository reservaRepository, MessageSource messageSource) {
        this.parkingRepository = parkingRepository;
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
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
     * Obtiene el parking asociado a la empresa de un usuario.
     *
     * <p>Flujo:</p>
     * <ol>
     *   <li>Busca el usuario por su email.</li>
     *   <li>Verifica que el usuario tenga una empresa asociada.</li>
     *   <li>Obtiene el  parkings vinculados a la empresa.</li>
     *   <li>Transforma las entidades en DTOs de respuesta.</li>
     * </ol>
     *
     * @param email correo electrónico del usuario.
     * @param id del parking.
     * @return  {@link ParkingDtoResponse} con los parkings de la empresa.
     * @throws UsernameNotFoundException si el usuario no existe o no tiene empresa asociada.
     * @throws RuntimeException si el parking no existe o esta asociada a la empresa.
     */
    public ParkingDtoResponse findByCompanyIdAndId(String email, Long id) {
        Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

        if (user.getEmpresa() == null || user.getEmpresa().getId() == null) {
            logger.error("El usuario con email " + email + " no tiene empresa asociada");
            throw new UsernameNotFoundException(
                    messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale()));
        }

        Parking parking = parkingRepository.findByIdAndEmpresaId(id, user.getEmpresa().getId().longValue());
        if (parking == null) {
        	throw new RuntimeException(
                    messageSource.getMessage("error.parking.no.existe", null, LocaleContextHolder.getLocale()));
        }
        
        ParkingDtoResponse response = new ParkingDtoResponse(parking);
        return response;
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
        	    Specification.where(ParkingSpecs.porId(request.getId()))
        	        .and(ParkingSpecs.porProvincia(request.getProvincia()))
        	        .and(ParkingSpecs.porMunicipio(request.getLocalidad()))
        	        .and(ParkingSpecs.porActivo(true))
        	        .and(ParkingSpecs.conElectricidad(request.isTomaElectricidad()))
        	        .and(ParkingSpecs.conResiduales(request.isLimpiezaAguasResiduales()))
        	        .and(ParkingSpecs.conVips(request.isPlazasVip()))
        	        .and(ParkingSpecs.conPlazasDisponibles(request.getFechaDesde(), request.getFechaHasta()))
        );

        List<ParkingDtoFindResponse> lista= parkings.stream()
        .map(p -> new ParkingDtoFindResponse(p, request.getFechaDesde(), request.getFechaHasta()))
        .toList();
        for (int i=0; i<lista.size();i++) {
        	lista.get(i).setMedia(reservaRepository.mediaReservas(lista.get(i).getId()));
        }
        return lista;
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
    
    /**
     * Busca un parking por su identificador único.
     *
     * @param id identificador del parking.
     * @return objeto {@link ParkingDtoResponse} con la información del parking.
     * @throws IllegalArgumentException si el parking no existe.
     */
    public ParkingDtoResponse findById(Long id) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        messageSource.getMessage("error.parking.no.existe", null, LocaleContextHolder.getLocale())));

        ParkingDtoResponse parkingResponse= new ParkingDtoResponse(parking);
        parkingResponse.setMedia(reservaRepository.mediaReservas(parkingResponse.getId()));
        return parkingResponse;
    }
    
    public void crear(String email, ParkingRequest request) {
        Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

        Empresa empresa = new Empresa(); empresa.setId(user.getEmpresa().getId());
      
        Parking parking = new Parking();
        parking.setEmpresa(empresa);
       
        parking.setEmail(request.getEmailParking());
        parking.setMunicipio(request.getMunicipioParking());
        parking.setNombre(request.getNombreParking());
        parking.setPersonaContacto(request.getPersonaContactoParking());
        parking.setProvincia(request.getProvinciaParking());
        parking.setTelefono(request.getTelefonoParking());
        
        parking.setActivo(false);
        if (request.getIsActivoParking() != null) {
        	parking.setTieneElectricidad(request.getIsActivoParking());
        }
        
        if (request.getTieneElectricidadParking() != null) {
        	parking.setTieneElectricidad(request.getTieneElectricidadParking());
        }
        if (request.getTienePlazasVipParking() != null) {
        	parking.setTieneVips(request.getTienePlazasVipParking());
        }
        if (request.getTieneResidualesParking() != null) {
        	parking.setTieneResiduales(request.getTieneResidualesParking());
        }
        
        parkingRepository.save(parking);
    }
    
    
    public void update(String email, ParkingUpdateRequest request) {
        Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));

        Empresa empresa = new Empresa(); empresa.setId(user.getEmpresa().getId());
        Parking parking = parkingRepository.findByIdAndEmpresaId(request.getIdParking(),user.getEmpresa().getId());
      
        if (parking == null) {
        	throw new RuntimeException(
                    messageSource.getMessage("error.parking.no.existe", null, LocaleContextHolder.getLocale()));
        }
        
        parking.setEmpresa(empresa);
       
        if (request.getEmailParking() != null) {
        	 parking.setEmail(request.getEmailParking());
        }
        
        if (request.getMunicipioParking() != null) {
       	 parking.setMunicipio(request.getMunicipioParking());
        }
        if (request.getNombreParking() != null) {
          	 parking.setNombre(request.getNombreParking());
        }
        
        if (request.getPersonaContactoParking() != null) {
         	 parking.setPersonaContacto(request.getPersonaContactoParking());
        }
        
        if (request.getProvinciaParking() != null) {
        	 parking.setProvincia(request.getProvinciaParking());
        }
        
        if (request.getTelefonoParking() != null) {
        	 parking.setTelefono(request.getTelefonoParking());
        }
               
        if (request.getIsActivoParking() != null) {
        	parking.setTieneElectricidad(request.getIsActivoParking());
        }
        
        if (request.getTieneElectricidadParking() != null) {
        	parking.setTieneElectricidad(request.getTieneElectricidadParking());
        }
        if (request.getTienePlazasVipParking() != null) {
        	parking.setTieneVips(request.getTienePlazasVipParking());
        }
        if (request.getTieneResidualesParking() != null) {
        	parking.setTieneResiduales(request.getTieneResidualesParking());
        }
        
        parkingRepository.save(parking);
    }
}
