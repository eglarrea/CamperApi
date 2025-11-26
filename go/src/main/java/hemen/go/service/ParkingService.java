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

@Service
public class ParkingService {
	private static final Logger logger = LoggerFactory.getLogger(ParkingService.class);
	private final ParkingRepository parkingRepository;
	private final UsuarioRepository usuarioRepository;
	private final FechaValidator fechaValidator;
	private final MessageSource messageSource;

	public ParkingService(ParkingRepository parkingRepository, UsuarioRepository usuarioRepository,
			FechaValidator fechaValidator, MessageSource messageSource) {
		this.parkingRepository = parkingRepository;
		this.usuarioRepository = usuarioRepository;
		this.fechaValidator = fechaValidator;
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

	/*
	 * public FilterParkingResponse findParkings(FilterParkingRequest request) { //
	 * Simulación de respuesta (mock) List<ParkingDtoResponse> mockParkings =
	 * List.of( new ParkingDtoResponse(1L, "Parking Centro", "Bilbao", true, false,
	 * true), new ParkingDtoResponse(2L, "Parking Playa", "Getxo", false, true,
	 * false), new ParkingDtoResponse(3L, "Parking Aeropuerto", "Loiu", true, true,
	 * true) );
	 * 
	 * return new FilterParkingResponse(mockParkings); }
	 */

	public List<ParkingDtoFindResponse> findParkings(FilterParkingRequest request) {
		/*
		 * List<Parking> parkings = parkingRepository.findAll(
		 * 
		 * Specification.where(ParkingSpecs.conPlazasDisponibles(request.getFechaDesde()
		 * , request.getFechaHasta()))
		 * .and(ParkingSpecs.porProvincia(request.getPronvincia()))
		 * .and(ParkingSpecs.porMunicipio(request.getLocalidad()))
		 * .and(ParkingSpecs.porActivo(true))
		 * .and(ParkingSpecs.conElectricidad(request.isLimpiezaAguasResiduales()))
		 * .and(ParkingSpecs.conResiduales(request.isLimpiezaAguasResiduales()))
		 * .and(ParkingSpecs.conVips(request.isPlazasVip())) );
		 */
		/*
		 * LocalDate hoy = LocalDate.now(); if (request.getFechaDesde() != null &&
		 * request.getFechaDesde().isBefore(hoy)) { throw new IllegalArgumentException(
		 * messageSource.getMessage("error.fecha.anterior.actual", null,
		 * LocaleContextHolder.getLocale())); } if (request.getFechaHasta() != null) {
		 * if (request.getFechaHasta().isBefore(hoy)) { throw new
		 * IllegalArgumentException(
		 * messageSource.getMessage("error.fecha.anterior.actual", null,
		 * LocaleContextHolder.getLocale())); } if (request.getFechaDesde() != null &&
		 * request.getFechaHasta().isBefore(request.getFechaDesde())) { throw new
		 * IllegalArgumentException(
		 * messageSource.getMessage("error.fecha.anterior.actual", null,
		 * LocaleContextHolder.getLocale())); } }
		 */
		fechaValidator.validarFechas(request.getFechaDesde(), request.getFechaHasta());

		List<Parking> parkings = parkingRepository
				.findAll(Specification.where(ParkingSpecs.porProvincia(request.getPronvincia()))
						.and(ParkingSpecs.porMunicipio(request.getLocalidad())).and(ParkingSpecs.porActivo(true))
						.and(ParkingSpecs.conElectricidad(request.isTomaElectricidad()))
						.and(ParkingSpecs.conResiduales(request.isLimpiezaAguasResiduales()))
						.and(ParkingSpecs.conVips(request.isPlazasVip()))
						.and(ParkingSpecs.conPlazasDisponibles(request.getFechaDesde(), request.getFechaHasta())));

		List<ParkingDtoFindResponse> plazasResponse = parkings.stream()
				.map(p -> new ParkingDtoFindResponse(p, request.getFechaDesde(), request.getFechaHasta())).toList();

		return plazasResponse;
	}

	public List<ParkingDtoResponse> findAll() {
		List<Parking> listParking = parkingRepository.findAll();
		return listParking.stream().map(ParkingDtoResponse::new).toList();
	}

}
