package hemen.go.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hemen.go.dto.request.FilterParkingRequest;
import hemen.go.dto.request.PlazaRequest;
import hemen.go.dto.response.FilterParkingResponse;
import hemen.go.dto.response.ParkingDtoResponse;
import hemen.go.dto.response.PlazaResponse;
import hemen.go.entity.Parking;
import hemen.go.entity.Plaza;
import hemen.go.entity.Usuario;
import hemen.go.repository.ParkingRepository;
import hemen.go.repository.PlazaRepository;
import hemen.go.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PlazaService {
	 private static final Logger logger = LoggerFactory.getLogger(PlazaService.class);
	 private final ParkingRepository parkingRepository;
	 private final PlazaRepository plazaRepository;
	 private final UsuarioRepository usuarioRepository;
	 private final MessageSource messageSource;
	
	public PlazaService(ParkingRepository parkingRepository,PlazaRepository plazaRepository,UsuarioRepository usuarioRepository, MessageSource messageSource) {
		this.parkingRepository = parkingRepository;
		this.plazaRepository = plazaRepository;
        this.usuarioRepository = usuarioRepository;
        this.messageSource = messageSource;
    }
	
	public PlazaResponse findByCompanyAndPlazaId(String email,Long idPlaza) {
		Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
				messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
		if (null == user.getEmpresa() || null == user.getEmpresa().getId()) {
			logger.error("El usuario email" + email + " no tiene empresa asociada");
			throw new UsernameNotFoundException(
					messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale()));
		}
		return plazaRepository.findByIdAndParking_Empresa_Id(user.getEmpresa().getId().longValue(),idPlaza).map(PlazaResponse::new).orElseThrow(() -> new UsernameNotFoundException(
				messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
	}
	
	public FilterParkingResponse findParkings(FilterParkingRequest request) {
	        // Simulación de respuesta (mock)
	        List<ParkingDtoResponse> mockParkings = List.of(
	            new ParkingDtoResponse(1L, "Parking Centro", "Bilbao", true, false, true),
	            new ParkingDtoResponse(2L, "Parking Playa", "Getxo", false, true, false),
	            new ParkingDtoResponse(3L, "Parking Aeropuerto", "Loiu", true, true, true)
	        );

	        return new FilterParkingResponse(mockParkings);
	    }
	 
	 public List<Parking> findAll() {
		 	List<Parking>  listParking = parkingRepository.findAll();

	        return listParking;
	 }
	 
	 public PlazaResponse addPlazaToParking(String email,Long parkingId, PlazaRequest request) {
		 
		 	Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
					messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
			if (null == user.getEmpresa() || null == user.getEmpresa().getId()) {
				logger.error("El usuario email" + email + " no tiene empresa asociada");
				throw new UsernameNotFoundException(
						messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale()));
			}
	        // Buscar el parking
	        Parking parking = parkingRepository.findById(parkingId)
	                .orElseThrow(() -> new EntityNotFoundException("Parking no encontrado"));

	        // Crear nueva plaza
	        Plaza plaza = new Plaza();
	        plaza.setNombre(request.getNombre());
	        plaza.setEsVip(request.isEsVip());
	        plaza.setTieneElectricidad(request.isTieneElectricidad());
	        plaza.setEstado(request.getEstado());
	        plaza.setPrecio(request.getPrecio());
	        plaza.setParking(parking);

	        // Guardar plaza
	        Plaza saved = plazaRepository.save(plaza);

	        return new PlazaResponse(saved);
	    }
	 
	 	public PlazaResponse updatePlaza(String email, Long parkingId, Long plazaId, PlazaRequest request) {
		    
		    // Validar usuario
		    Usuario user = usuarioRepository.findByEmailPersona(email)
		            .orElseThrow(() -> new UsernameNotFoundException(
		                    messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
		    
		    if (user.getEmpresa() == null || user.getEmpresa().getId() == null) {
		        logger.error("El usuario con email " + email + " no tiene empresa asociada");
		        throw new UsernameNotFoundException(
		                messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale()));
		    }

		    // Validar parking
		    Parking parking = parkingRepository.findById(parkingId)
		            .orElseThrow(() -> new EntityNotFoundException("Parking no encontrado"));

		    // Validar plaza
		    Plaza plaza = plazaRepository.findById(plazaId)
		            .orElseThrow(() -> new EntityNotFoundException("Plaza no encontrada"));

		    // Comprobar que la plaza pertenece al parking indicado
		    if (!plaza.getParking().getId().equals(parking.getId())) {
		        throw new IllegalArgumentException("La plaza no pertenece al parking especificado");
		    }

		    // Actualizar datos de la plaza
		    plaza.setNombre(request.getNombre());
		    plaza.setEsVip(request.isEsVip());
		    plaza.setTieneElectricidad(request.isTieneElectricidad());
		    plaza.setEstado(request.getEstado());
		    plaza.setPrecio(request.getPrecio());

		    // Guardar cambios
		    Plaza updated = plazaRepository.save(plaza);

		    return new PlazaResponse(updated);
		}
	 

}
