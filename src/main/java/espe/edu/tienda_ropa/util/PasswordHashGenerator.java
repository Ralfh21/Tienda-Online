package espe.edu.tienda_ropa.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=== HASHES DE CONTRASEÑAS ===\n");

        // Admin
        String admin123 = encoder.encode("admin123");
        System.out.println("admin123:");
        System.out.println(admin123);
        System.out.println();

        // Cliente
        String cliente123 = encoder.encode("cliente123");
        System.out.println("cliente123:");
        System.out.println(cliente123);
        System.out.println();

        // Password123
        String password123 = encoder.encode("password123");
        System.out.println("password123:");
        System.out.println(password123);
        System.out.println();
    }
}

