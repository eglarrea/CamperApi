package hemen.go.service.specification;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import hemen.go.entity.Plaza;
import hemen.go.entity.Reserva;
import hemen.go.enums.EstadoReserva;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class PlazaSpecs {

    public static Specification<Plaza> porProvincia(String provincia) {
        return (root, query, cb) ->
            provincia == null || provincia.isBlank()
                ? cb.conjunction()
                : cb.equal(root.get("parking").get("provincia"), provincia);
    }

    public static Specification<Plaza> porMunicipio(String municipio) {
        return (root, query, cb) ->
            municipio == null || municipio.isBlank()
                ? cb.conjunction()
                : cb.equal(root.get("parking").get("municipio"), municipio);
    }

    public static Specification<Plaza> conElectricidad(Boolean tieneElectricidad) {
        return (root, query, cb) ->
            tieneElectricidad == null
                ? cb.conjunction()
                : cb.equal(root.get("tieneElectricidad"), tieneElectricidad);
    }

    public static Specification<Plaza> conResiduales(Boolean tieneResiduales) {
        return (root, query, cb) ->
            tieneResiduales == null
                ? cb.conjunction()
                : cb.equal(root.get("parking").get("tieneResiduales"), tieneResiduales);
    }

    public static Specification<Plaza> conVips(Boolean esVip) {
        return (root, query, cb) ->
            esVip == null
                ? cb.conjunction()
                : cb.equal(root.get("esVip"), esVip);
    }

    public static Specification<Plaza> disponibleEntre(LocalDate fechaInicio, LocalDate fechaFin) {
        return (root, query, cb) -> {
            if (fechaInicio == null || fechaFin == null) {
                return cb.conjunction(); // si no envían fechas, no se filtra
            }

            // Subquery para reservas activas que solapan
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Reserva> reservaRoot = subquery.from(Reserva.class);

            subquery.select(reservaRoot.get("id"))
                    .where(
                        cb.equal(reservaRoot.get("plaza"), root),
                        cb.equal(reservaRoot.get("estado"), EstadoReserva.ACTIVA.getCodigo()), // solo reservas activas
                        cb.lessThanOrEqualTo(reservaRoot.get("fecInicio"), fechaFin),
                        cb.greaterThanOrEqualTo(reservaRoot.get("fecFin"), fechaInicio)
                    );

            return cb.not(cb.exists(subquery));
        };
    }
}
