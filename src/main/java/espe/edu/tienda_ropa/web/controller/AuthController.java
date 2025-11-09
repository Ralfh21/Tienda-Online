package espe.edu.tienda_ropa.web.controller;

import espe.edu.tienda_ropa.config.JwtUtil;
import espe.edu.tienda_ropa.domain.Cliente;
import espe.edu.tienda_ropa.domain.Rol;
import espe.edu.tienda_ropa.domain.Usuario;
import espe.edu.tienda_ropa.repository.ClienteDomainRepository;
import espe.edu.tienda_ropa.repository.RolRepository;
import espe.edu.tienda_ropa.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ClienteDomainRepository clienteRepository;

    public AuthController(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
                          ClienteDomainRepository clienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String nombre = body.get("nombre");
        String rolNombre = body.getOrDefault("rol", "ROLE_USER");

        if (usuarioRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "El email ya está registrado."));
        }

        // Buscar o crear el rol
        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseGet(() -> rolRepository.save(new Rol(rolNombre)));

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setNombre(nombre);
        usuario.setRoles(Collections.singleton(rol));
        usuario = usuarioRepository.save(usuario);

        // Si es ROLE_USER, crear también un Cliente asociado
        if ("ROLE_USER".equals(rolNombre)) {
            Cliente cliente = new Cliente();
            String[] nombreCompleto = nombre.split(" ", 2);
            cliente.setNombre(nombreCompleto[0]);
            cliente.setApellido(nombreCompleto.length > 1 ? nombreCompleto[1] : "");
            cliente.setEmail(email);
            cliente.setUsuario(usuario);
            clienteRepository.save(cliente);
        }

        return ResponseEntity.ok(Map.of("message", "Usuario registrado correctamente."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado."));
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta."));
        }

        String token = jwtUtil.generateToken(email);

        // Obtener todos los roles del usuario
        List<String> roles = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", usuario.getEmail());
        response.put("nombre", usuario.getNombre());
        response.put("roles", roles);
        response.put("userId", usuario.getId());

        // Si es cliente, agregar información adicional
        if (usuario.getCliente() != null) {
            response.put("clienteId", usuario.getCliente().getId());
        }

        return ResponseEntity.ok(response);
    }
}
