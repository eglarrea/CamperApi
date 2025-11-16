package hemen.go.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hemen.go.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
