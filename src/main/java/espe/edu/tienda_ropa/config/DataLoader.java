package espe.edu.tienda_ropa.config;

import espe.edu.tienda_ropa.domain.Rol;
import espe.edu.tienda_ropa.domain.Usuario;
import espe.edu.tienda_ropa.repository.RolRepository;
import espe.edu.tienda_ropa.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepo, RolRepository rolRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepo = rolRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // ============================
        // Crear roles si no existen
        // ============================
        Rol rolCliente = rolRepo.findByNombre("CLIENTE")
                .orElseGet(() -> rolRepo.save(new Rol("CLIENTE")));

        Rol rolAdmin = rolRepo.findByNombre("ADMIN")
                .orElseGet(() -> rolRepo.save(new Rol("ADMIN")));

        // ============================
        // Usuario CLIENTE
        // ============================
        if (!usuarioRepo.existsByEmail("cliente@tiendaropa.com")) {

            Usuario cliente = new Usuario();
            cliente.setNombre("Cliente Ejemplo");
            cliente.setEmail("cliente@tiendaropa.com");
            cliente.setPassword(passwordEncoder.encode("cliente123"));
            cliente.setRoles(Collections.singleton(rolCliente));

            usuarioRepo.save(cliente);
            System.out.println("✔ Cliente creado");
        }

        // ============================
        // Usuario ADMIN
        // ============================
        if (!usuarioRepo.existsByEmail("admin@tiendaropa.com")) {

            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@tiendaropa.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Collections.singleton(rolAdmin));

            usuarioRepo.save(admin);
            System.out.println("✔ Admin creado");
        }
    }
}
