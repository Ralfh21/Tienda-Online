package espe.edu.tienda_ropa.web.controller;

import espe.edu.tienda_ropa.dto.ClienteRequestData;
import espe.edu.tienda_ropa.dto.ClienteResponse;
import espe.edu.tienda_ropa.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteRequestData request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> getAll() {
        return ResponseEntity.ok(clienteService.list());
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ClienteResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.deactivate(id));
    }
}
