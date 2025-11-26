package hemen.go.enums;

public enum EstadoPlaza {
    ALTA("0"),
    BAJA("1");

    private final String codigo;

    EstadoPlaza(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static EstadoPlaza fromCodigo(String codigo) {
        for (EstadoPlaza estado : values()) {
            if (estado.codigo.equals(codigo)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código de estado desconocido: " + codigo);
    }
}
