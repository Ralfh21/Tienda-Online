package espe.edu.tienda_ropa.web.controller;

import espe.edu.tienda_ropa.dto.DetallePedidoRequestData;
import espe.edu.tienda_ropa.dto.DetallePedidoResponse;
import espe.edu.tienda_ropa.service.DetallePedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-pedidos")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @PostMapping
    public ResponseEntity<DetallePedidoResponse> create(@Valid @RequestBody DetallePedidoRequestData request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detallePedidoService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedidoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(detallePedidoService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<DetallePedidoResponse>> getAll() {
        return ResponseEntity.ok(detallePedidoService.list());
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<DetallePedidoResponse>> getByPedidoId(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(detallePedidoService.findByPedidoId(pedidoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        detallePedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
