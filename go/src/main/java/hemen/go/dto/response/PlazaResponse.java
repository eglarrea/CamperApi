package hemen.go.dto.response;

public class PlazaResponse {
    private Long id;
    private String nombre;
    private boolean esVip;
    private boolean tieneElectricidad;
    private String estado;
    private float precio;
    private String parkingNombre; // opcional: mostrar el nombre del parking

    // Constructor desde entidad Plaza
    public PlazaResponse(hemen.go.entity.Plaza plaza) {
        this.id = plaza.getId();
        this.nombre = plaza.getNombre();
        this.esVip = plaza.isEsVip();
        this.tieneElectricidad = plaza.isTieneElectricidad();
        this.estado = plaza.getEstado() != null ? plaza.getEstado().getCodigo() : null;
        this.precio = plaza.getPrecio();
        this.parkingNombre = plaza.getParking() != null ? plaza.getParking().getNombre() : null;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public boolean isEsVip() { return esVip; }
    public void setEsVip(boolean esVip) { this.esVip = esVip; }

    public boolean isTieneElectricidad() { return tieneElectricidad; }
    public void setTieneElectricidad(boolean tieneElectricidad) { this.tieneElectricidad = tieneElectricidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public float getPrecio() { return precio; }
    public void setPrecio(float precio) { this.precio = precio; }

    public String getParkingNombre() { return parkingNombre; }
    public void setParkingNombre(String parkingNombre) { this.parkingNombre = parkingNombre; }
}