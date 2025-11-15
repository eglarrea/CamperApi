package hemen.go.service;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import hemen.go.dto.request.FilterParkingRequest;
import hemen.go.dto.response.FilterParkingResponse;
import hemen.go.dto.response.ParkingDtoResponse;
import hemen.go.entity.Parking;
import hemen.go.repository.ParkingRepository;

@Service
public class ParkingService {
	 private final ParkingRepository parkingRepository;
	 private final MessageSource messageSource;
	
	public ParkingService(ParkingRepository parkingRepository, MessageSource messageSource) {
        this.parkingRepository = parkingRepository;
        this.messageSource = messageSource;
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
	        

	        return parkingRepository.findAll();
	    }
}
