package hemen.go.dto.request;

public class PuntuarReservaRequest {

	    private Long idReserva;
	    
	    private Integer puntuacion;
	    
		public Long getIdReserva() {
			return idReserva;
		}
		public void setIdReserva(Long idReserva) {
			this.idReserva = idReserva;
		}
		public Integer getPuntuacion() {
			return puntuacion;
		}
		public void setPuntuacion(Integer puntuacion) {
			this.puntuacion = puntuacion;
		}
	   
}
