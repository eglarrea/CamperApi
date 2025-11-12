package hemen.go.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hemen.go.entity.User;

public interface UsuarioRepository extends JpaRepository<User, Long> {
	  boolean existsByEmailPersona(String emailPersona);
	  Optional<User> findByEmailPersona(String email);
}
