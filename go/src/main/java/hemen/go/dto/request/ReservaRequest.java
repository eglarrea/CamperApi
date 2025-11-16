package hemen.go.dto.request;
import java.sql.Date;

public class ReservaRequest {

	    private Long idPlaza;
	    private Long idParking;
	    private Date fecInicio;
	    private Date fecFin;
	    
		public Long getIdPlaza() {
			return idPlaza;
		}
		public void setIdPlaza(Long idPlaza) {
			this.idPlaza = idPlaza;
		}
		public Long getIdParking() {
			return idParking;
		}
		public void setIdParking(Long idParking) {
			this.idParking = idParking;
		}
		public Date getFecInicio() {
			return fecInicio;
		}
		public void setFecInicio(Date fecInicio) {
			this.fecInicio = fecInicio;
		}
		public Date getFecFin() {
			return fecFin;
		}
		public void setFecFin(Date fecFin) {
			this.fecFin = fecFin;
		}
	   
}
