package hemen.go;

import hemen.go.entity.User;
import hemen.go.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void guardarUsuarioYBuscarPorEmail() {
    	User usuario = new User();
        usuario.setNombre("Ane");
        usuario.setApellidos("");
        usuario.setFechaNacimiento(LocalDate.of(1990, 5, 12));
        usuario.setEmail("ane@example.com");
        usuario.setContrasena("secreta123");

        usuarioRepository.save(usuario);

        boolean existe = usuarioRepository.existsByEmail("an5e@example.com");
        assertThat(existe).isTrue();
    }
}
