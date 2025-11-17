package hemen.go.dto.request;

public class TokenRequest {
	private String token;
	private Long idParking;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getIdParking() {
		return idParking;
	}

	public void setIdParking(Long idParking) {
		this.idParking = idParking;
	}
}
