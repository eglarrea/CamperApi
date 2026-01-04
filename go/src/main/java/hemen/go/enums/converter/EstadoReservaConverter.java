package hemen.go.enums.converter;

import hemen.go.enums.EstadoReserva;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoReservaConverter implements AttributeConverter<EstadoReserva, String> {

    @Override
    public String convertToDatabaseColumn(EstadoReserva estado) {
        if (estado == null) {
            return null;
        }
        return estado.getCodigo(); // guarda "0" o "1"
    }

    @Override
    public EstadoReserva convertToEntityAttribute(String codigo) {
        if (codigo == null) {
            return null;
        }
        return EstadoReserva.fromCodigo(codigo); // convierte "0"/"1" a enum
    }
}
