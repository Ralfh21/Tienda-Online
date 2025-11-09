package espe.edu.tienda_ropa.web.controller;

import espe.edu.tienda_ropa.dto.PedidoRequestData;
import espe.edu.tienda_ropa.dto.PedidoResponse;
import espe.edu.tienda_ropa.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> create(@Valid @RequestBody PedidoRequestData request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> getAll() {
        return ResponseEntity.ok(pedidoService.list());
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<PedidoResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancel(id));
    }
}
