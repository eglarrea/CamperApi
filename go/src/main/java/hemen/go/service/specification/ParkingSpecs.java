package hemen.go.service.specification;
import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import hemen.go.entity.Parking;
import hemen.go.entity.Plaza;
import hemen.go.entity.Reserva;
import hemen.go.enums.EstadoPlaza;
import hemen.go.enums.EstadoReserva;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
public class ParkingSpecs {
	
	public static Specification<Parking> porId(Long idParking) {
        return (root, query, cb) ->
            idParking == null
                ? cb.conjunction()	
                : cb.equal(root.get("id"), idParking);
    }

    public static Specification<Parking> porProvincia(String provincia) {
        return (root, query, cb) ->
            provincia == null || provincia.isBlank()
                ? cb.conjunction()
                		: cb.like( cb.lower(root.get("provincia")), "%" + provincia.trim().toLowerCase() + "%" );
    }

    public static Specification<Parking> porMunicipio(String municipio) {
        return (root, query, cb) ->
            municipio == null || municipio.isBlank()
                ? cb.conjunction()
                		: cb.like( cb.lower(root.get("municipio")), "%" + municipio.trim().toLowerCase() + "%" );
    }

    public static Specification<Parking> porActivo(Boolean activo) {
        return (root, query, cb) ->
            activo == null
                ? cb.conjunction()
                : cb.equal(root.get("isActivo"), activo);
    }

    public static Specification<Parking> conElectricidad(Boolean tieneElectricidad) {
        return (root, query, cb) ->
            tieneElectricidad == null
                ? cb.conjunction()
                : cb.equal(root.get("tieneElectricidad"), tieneElectricidad);
    }

    public static Specification<Parking> conResiduales(Boolean tieneResiduales) {
        return (root, query, cb) ->
            tieneResiduales == null
                ? cb.conjunction()
                : cb.equal(root.get("tieneResiduales"), tieneResiduales);
    }

    public static Specification<Parking> conVips(Boolean tieneVips) {
        return (root, query, cb) ->
            tieneVips == null
                ? cb.conjunction()
                : cb.equal(root.get("tieneVips"), tieneVips);
    }

    public static Specification<Parking> conPlazasDisponibles(LocalDate fechaInicio, LocalDate fechaFin) {
        return (root, query, cb) -> {
            if (fechaInicio == null || fechaFin == null) {
                return cb.conjunction(); // si no envían fechas, no se filtra
            }

            Join<Parking, Plaza> plazas = root.join("plazas");
            Predicate plazaLibre = cb.equal(plazas.get("estado"), EstadoPlaza.ALTA);
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Reserva> reservaRoot = subquery.from(Reserva.class);

            subquery.select(reservaRoot.get("id"))
                    .where(
                        cb.equal(reservaRoot.get("plaza"), plazas),
                        cb.equal(reservaRoot.get("estado"), EstadoReserva.ACTIVA.getCodigo()), // solo reservas activas
                        cb.lessThanOrEqualTo(reservaRoot.get("fecInicio"), fechaFin),
                        cb.greaterThanOrEqualTo(reservaRoot.get("fecFin"), fechaInicio)
                    );

            query.distinct(true);
            return cb.and(plazaLibre, cb.not(cb.exists(subquery)));
        };
    }
}

