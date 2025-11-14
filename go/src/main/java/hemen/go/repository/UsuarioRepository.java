package hemen.go.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import hemen.go.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	  boolean existsByEmailPersona(String emailPersona);
	  Optional<Usuario> findByEmailPersona(String email);
}
