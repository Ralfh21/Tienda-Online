package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.domain.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UsuarioService extends UserDetailsService {
    Usuario registrarUsuario(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
}
