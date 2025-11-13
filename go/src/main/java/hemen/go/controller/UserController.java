package hemen.go.controller;

import org.springframework.web.bind.annotation.*;

import hemen.go.entity.Usuario;
import hemen.go.repository.UsuarioRepository;

import org.springframework.validation.annotation.Validated;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UsuarioRepository usuarioRepository;

    public UserController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario crear(@Validated @RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}

