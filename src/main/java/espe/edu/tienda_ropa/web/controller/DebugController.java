package espe.edu.tienda_ropa.web.controller;

import espe.edu.tienda_ropa.repository.ClienteDomainRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final ClienteDomainRepository clienteRepository;

    public DebugController(ClienteDomainRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/count")
    public String countClientes() {
        try {
            long count = clienteRepository.count();
            return "Clientes en BD: " + count;
        } catch (Exception e) {
            return "Error al acceder BD: " + e.getMessage();
        }
    }

    @GetMapping("/tables")
    public String testConnection() {
        try {
            // Intenta hacer una consulta simple
            clienteRepository.findAll();
            return "Conexión a BD exitosa";
        } catch (Exception e) {
            return "Error conexión BD: " + e.getMessage();
        }
    }
}
