package hemen.go.dto.response;

public class ParkingDtoResponse {

	 private Long id;
	 private String nombre;
	 private String localidad;
	 private boolean tomaElectricidad;
	 private boolean limpiezaAguasResiduales;
	 private boolean plazasVip;
	 
	 public ParkingDtoResponse(){}
	 
	 public ParkingDtoResponse(Long id, String nombre, String localidad, boolean tomaElectricidad,
			boolean limpiezaAguasResiduales, boolean plazasVip) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.localidad = localidad;
		this.tomaElectricidad = tomaElectricidad;
		this.limpiezaAguasResiduales = limpiezaAguasResiduales;
		this.plazasVip = plazasVip;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public boolean isTomaElectricidad() {
		return tomaElectricidad;
	}

	public void setTomaElectricidad(boolean tomaElectricidad) {
		this.tomaElectricidad = tomaElectricidad;
	}

	public boolean isLimpiezaAguasResiduales() {
		return limpiezaAguasResiduales;
	}

	public void setLimpiezaAguasResiduales(boolean limpiezaAguasResiduales) {
		this.limpiezaAguasResiduales = limpiezaAguasResiduales;
	}

	public boolean isPlazasVip() {
		return plazasVip;
	}

	public void setPlazasVip(boolean plazasVip) {
		this.plazasVip = plazasVip;
	}
}
