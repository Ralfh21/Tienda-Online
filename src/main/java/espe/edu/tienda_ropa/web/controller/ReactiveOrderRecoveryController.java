package espe.edu.tienda_ropa.web.controller;

import espe.edu.tienda_ropa.reactive.ReactiveOrderRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/reactive/recovery")
public class ReactiveOrderRecoveryController {

    @Autowired
    private ReactiveOrderRecoveryService recoveryService;

    // ================================
    // PROBAR MANEJO DE ERRORES DE PEDIDOS
    // ================================
    @PostMapping("/order")
    public Mono<String> processOrderWithRecovery(
            @RequestParam double price,
            @RequestParam int quantity,
            @RequestParam int stock) {

        return recoveryService.handleOrderWithRecovery(price, quantity, stock);
    }

    // ================================
    // FLUJO REACTIVO CON ERRORES CONTROLADOS
    // ================================
    @GetMapping("/orders/flow")
    public Flux<Map<String, Object>> simulateOrderFlow() {
        return recoveryService.simulateOrderFlowWithErrors();
    }
}