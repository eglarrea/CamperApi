package hemen.go.dto.request;

import jakarta.validation.constraints.NotNull;

public class ParkingUpdateRequest extends ParkingRequest {
	@NotNull private Long idParking; 
	
	public ParkingUpdateRequest() {} 
	
	public Long getIdParking() { 
		return idParking; 
	}
	
	public void setIdParking(Long idParking) { 
		this.idParking = idParking;
	}
}

