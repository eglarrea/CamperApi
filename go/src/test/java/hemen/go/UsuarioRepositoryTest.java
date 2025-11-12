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
        usuario.setNombre_persona("Ane");
        usuario.setApellidos_persona("");
        usuario.setFec_nacimiento_persona(LocalDate.of(1990, 5, 12));
        usuario.setEmailPersona("ane@example.com");
        usuario.setPass_persona("secreta123");

        usuarioRepository.save(usuario);

        boolean existe = usuarioRepository.existsByEmailPersona("an5e@example.com");
        assertThat(existe).isTrue();
    }
}
