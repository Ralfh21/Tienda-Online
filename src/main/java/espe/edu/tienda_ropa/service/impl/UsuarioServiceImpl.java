package espe.edu.tienda_ropa.service.impl;

import espe.edu.tienda_ropa.domain.Rol;
import espe.edu.tienda_ropa.domain.Usuario;
import espe.edu.tienda_ropa.repository.RolRepository;
import espe.edu.tienda_ropa.repository.UsuarioRepository;
import espe.edu.tienda_ropa.service.UsuarioService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        String rol = usuario.getRoles().iterator().next().getNombre();

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(rol.replace("ROLE_", "")) // quita el prefijo ROLE_ para Spring
                .build();
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        Rol rolUsuario = rolRepository.findByNombre("ROLE_USER")
                .orElseGet(() -> rolRepository.save(new Rol("ROLE_USER")));
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRoles(Collections.singleton(rolUsuario));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
