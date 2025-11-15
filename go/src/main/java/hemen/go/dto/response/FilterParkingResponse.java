package hemen.go.dto.response;

import java.util.List;

public class FilterParkingResponse {

    private List<ParkingDtoResponse> parkings;

    public FilterParkingResponse(List<ParkingDtoResponse> parkings) {
        this.parkings = parkings;
    }

    public List<ParkingDtoResponse> getParkings() {
        return parkings;
    }

    public void setParkings(List<ParkingDtoResponse> parkings) {
        this.parkings = parkings;
    }
}