package hemen.go.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hemen.go.dto.request.FilterParkingRequest;
import hemen.go.dto.response.FilterParkingResponse;
import hemen.go.dto.response.ParkingDtoResponse;
import hemen.go.dto.response.UserDtoResponse;
import hemen.go.entity.Parking;
import hemen.go.entity.Usuario;
import hemen.go.repository.ParkingRepository;
import hemen.go.repository.UsuarioRepository;

@Service
public class ParkingService {
	 private static final Logger logger = LoggerFactory.getLogger(ParkingService.class);
	 private final ParkingRepository parkingRepository;
	 private final UsuarioRepository usuarioRepository;
	 private final MessageSource messageSource;
	
	public ParkingService(ParkingRepository parkingRepository,UsuarioRepository usuarioRepository, MessageSource messageSource) {
        this.parkingRepository = parkingRepository;
        this.usuarioRepository = usuarioRepository;
        this.messageSource = messageSource;
    }
	
	public List<ParkingDtoResponse> findByCompanyId(String email) {
		Usuario user = usuarioRepository.findByEmailPersona(email).orElseThrow(() -> new UsernameNotFoundException(
				messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale())));
		if (null == user.getEmpresa() || null == user.getEmpresa().getId()) {
			logger.error("El usuario email" + email + " no tiene empresa asociada");
			throw new UsernameNotFoundException(
					messageSource.getMessage("error.usuario.no.existe", null, LocaleContextHolder.getLocale()));
		}
		List<Parking> parking = parkingRepository.findByEmpresa_Id(user.getEmpresa().getId().longValue());
		return parking.stream().map(ParkingDtoResponse::new).toList();
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
	 

}
