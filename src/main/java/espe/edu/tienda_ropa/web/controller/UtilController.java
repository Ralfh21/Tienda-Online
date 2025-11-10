package espe.edu.tienda_ropa.web.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/util")
public class UtilController {

    private final PasswordEncoder passwordEncoder;

    public UtilController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/hash-password")
    public Map<String, String> hashPassword(@RequestBody Map<String, String> body) {
        String password = body.get("password");
        String hash = passwordEncoder.encode(password);
        return Map.of(
            "password", password,
            "hash", hash
        );
    }

    @GetMapping("/generate-hashes")
    public Map<String, Object> generateHashes() {
        Map<String, Object> result = new HashMap<>();

        // Generar hashes para las contraseñas del sistema
        String admin123Hash = passwordEncoder.encode("admin123");
        String cliente123Hash = passwordEncoder.encode("cliente123");
        String password123Hash = passwordEncoder.encode("password123");

        result.put("admin123", admin123Hash);
        result.put("cliente123", cliente123Hash);
        result.put("password123", password123Hash);

        // SQL listo para copiar y pegar
        StringBuilder sql = new StringBuilder();
        sql.append("-- USUARIOS CON CONTRASEÑAS CORRECTAS\n\n");
        sql.append("-- Admin\n");
        sql.append("INSERT INTO usuarios (email, password, nombre) VALUES\n");
        sql.append("('admin@tiendaropa.com', '").append(admin123Hash).append("', 'Administrador');\n\n");

        sql.append("-- Cliente\n");
        sql.append("INSERT INTO usuarios (email, password, nombre) VALUES\n");
        sql.append("('cliente@tiendaropa.com', '").append(cliente123Hash).append("', 'Cliente Ejemplo');\n\n");

        sql.append("-- Clientes adicionales\n");
        sql.append("INSERT INTO usuarios (email, password, nombre) VALUES\n");
        sql.append("('maria.garcia@email.com', '").append(password123Hash).append("', 'María García');\n\n");

        result.put("sql", sql.toString());

        return result;
    }
}

