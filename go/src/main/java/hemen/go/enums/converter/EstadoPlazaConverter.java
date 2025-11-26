package hemen.go.enums.converter;

import hemen.go.enums.EstadoPlaza;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoPlazaConverter implements AttributeConverter<EstadoPlaza, String> {

    @Override
    public String convertToDatabaseColumn(EstadoPlaza estado) {
        if (estado == null) {
            return null;
        }
        return estado.getCodigo(); // guarda "0" o "1"
    }

    @Override
    public EstadoPlaza convertToEntityAttribute(String codigo) {
        if (codigo == null) {
            return null;
        }
        return EstadoPlaza.fromCodigo(codigo); // convierte "0"/"1" a enum
    }
}
