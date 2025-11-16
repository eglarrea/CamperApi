package hemen.go.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hemen.go.entity.Parking;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
}
