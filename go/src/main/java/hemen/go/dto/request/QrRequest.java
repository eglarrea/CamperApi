package hemen.go.dto.request;

public class QrRequest {
	private Long idReserva;
	private String tokenReserva;
	 
	public Long getIdReserva() {
		return idReserva;
	}
	public void setIdReserva(Long idReserva) {
		this.idReserva = idReserva;
	}
	public String getTokenReserva() {
		return tokenReserva;
	}
	public void setTokenReserva(String tokenReserva) {
		this.tokenReserva = tokenReserva;
	}
}
