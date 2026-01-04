package hemen.go.enums;

public enum EstadoReserva {
    CANCELADA("0"),
    ACTIVA("1");

    private final String codigo;

    EstadoReserva(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static EstadoReserva fromCodigo(String codigo) {
        for (EstadoReserva estado : values()) {
            if (estado.codigo.equals(codigo)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código de estado reserva desconocido: " + codigo);
    }
}
