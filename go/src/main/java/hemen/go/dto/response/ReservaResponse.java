package hemen.go.dto.response;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ReservaResponse {
    private Long id;
    private Long usuarioId;
    private String usuarioEmail;
    private Long plazaId;
    private Long parkingId;
    private String parkingNombre;
    private float precio;
    private float precioTotal;
    private Date fecInicio;
    private Date fecFin;
    private Date fecAlta;
    private String estado;

    // Constructor que recibe la entidad Reserva
    public ReservaResponse(hemen.go.entity.Reserva reserva) {
        this.id = reserva.getId();

        if (reserva.getPersona() != null) {
            this.usuarioId = reserva.getPersona().getId();
            this.usuarioEmail = reserva.getPersona().getEmailPersona();
        }

        if (reserva.getPlaza() != null) {
            this.plazaId = reserva.getPlaza().getId();
            this.precio = reserva.getPlaza().getPrecio();
            if (reserva.getPlaza().getParking() != null) {
                this.parkingId = reserva.getPlaza().getParking().getId();
                this.parkingNombre = reserva.getPlaza().getParking().getNombre();
            }
        }

        this.fecInicio = reserva.getFecInicio();
        this.fecFin = reserva.getFecFin();
        this.fecAlta = reserva.getFecAlta();
        this.estado = reserva.getEstado();

        // Calcular precioTotal = precio * número de días
        if (fecInicio != null && fecFin != null && precio > 0) {
            long diffMillis = fecFin.getTime() - fecInicio.getTime();
            long dias = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS) + 1;
            // si la reserva es el mismo día, aseguramos al menos 1 día
            if (dias <= 0) dias = 1;
            this.precioTotal = precio * dias;
        } else {
            this.precioTotal = 0;
        }
    }

    // Getters
    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public Long getPlazaId() { return plazaId; }
    public float getPrecio() { return precio; }
    public float getPrecioTotal() { return precioTotal; }   // getter nuevo
    public Long getParkingId() { return parkingId; }
    public String getParkingNombre() { return parkingNombre; }
    public Date getFecInicio() { return fecInicio; }
    public Date getFecFin() { return fecFin; }
    public Date getFecAlta() { return fecAlta; }
    public String getEstado() { return estado; }
    
    @Override
    public String toString() {
        return "ReservaResponse{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", usuarioEmail='" + usuarioEmail + '\'' +
                ", plazaId=" + plazaId +
                ", parkingId=" + parkingId +
                ", parkingNombre='" + parkingNombre + '\'' +
                ", precio=" + precio +
                ", precioTotal=" + precioTotal +
                ", fecInicio=" + fecInicio +
                ", fecFin=" + fecFin +
                ", fecAlta=" + fecAlta +
                ", estado='" + estado + '\'' +
                '}';
    }
}
