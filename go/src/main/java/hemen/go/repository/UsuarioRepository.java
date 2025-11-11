package hemen.go.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hemen.go.entity.User;

public interface UsuarioRepository extends JpaRepository<User, Long> {
	  boolean existsByEmail(String email);
}
