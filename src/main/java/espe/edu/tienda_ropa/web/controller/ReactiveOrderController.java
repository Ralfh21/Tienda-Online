package espe.edu.tienda_ropa.web.controller;

import espe.edu.tienda_ropa.reactive.OrderSubscriber;
import espe.edu.tienda_ropa.reactive.ProductSubscriber;
import espe.edu.tienda_ropa.reactive.ReactiveProductService;
import espe.edu.tienda_ropa.reactive.ReactiveOrderService;
import espe.edu.tienda_ropa.domain.Pedido;
import espe.edu.tienda_ropa.domain.DetallePedido;
import espe.edu.tienda_ropa.repository.PedidoDomainRepository;
import espe.edu.tienda_ropa.repository.DetallePedidoDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/reactive")
public class ReactiveOrderController {

    @Autowired
    private ReactiveProductService reactiveProductService;

    @Autowired
    private ReactiveOrderService reactiveOrderService;

    @Autowired
    private PedidoDomainRepository pedidoRepository;

    @Autowired
    private DetallePedidoDomainRepository detallePedidoRepository;

    // ================================
    // DEMOSTRACIÓN BÁSICA DE FLUJO REACTIVO
    // ================================
    @GetMapping("/orders/demo")
    public String runReactiveFlow() {
        System.out.println("\nINICIANDO FLUJO REACTIVO DE PEDIDOS");
        System.out.println("Tiempo de inicio: " + LocalDateTime.now());

        Flux<Double> pedidos = Flux.just(2.50, 5.00, 12.00, 1.00, 25.00, 8.50)
                .delayElements(Duration.ofMillis(400))
                .filter(p -> {
                    System.out.println("Filtro: evaluando pedido de $" + p);
                    return p >= 5.00;
                })
                .map(p -> {
                    if (p > 20.00) {
                        System.out.println("Pedido inválido detectado: $" + p);
                        throw new RuntimeException("Pedido inválido: $" + p);
                    }
                    double impuesto = p * 1.12;
                    System.out.println("Calculando impuestos: $" + p + " -> $" + impuesto);
                    return impuesto;
                })
                .onErrorResume(err -> {
                    System.out.println("Manejo de error: " + err.getMessage());
                    System.out.println("Aplicando valores de respaldo...");
                    return Flux.just(6.00, 7.00, 8.00);
                });

        OrderSubscriber subscriber = new OrderSubscriber(2);
        pedidos.subscribe(subscriber);

        System.out.println("========================================================\n");
        return "Flujo reactivo iniciado. Revisa la consola para ver el procesamiento.";
    }

    // ================================
    // FLUJO REACTIVO DE PRODUCTOS CON SUBSCRIBER PERSONALIZADO
    // ================================
    @GetMapping("/products/demo")
    public String runProductReactiveFlow() {
        System.out.println("\nFLUJO REACTIVO DE PRODUCTOS");

        Flux<String> productFlux = reactiveProductService.getProductsReactively()
                .doOnSubscribe(subscription -> System.out.println("Suscripción iniciada para productos"))
                .doOnComplete(() -> System.out.println("Flujo de productos completado"));

        ProductSubscriber productSubscriber = new ProductSubscriber(3);
        productFlux.subscribe(productSubscriber);

        System.out.println("=================================================\n");
        return "Flujo reactivo de productos iniciado. Revisa la consola.";
    }

    // ================================
    // PROCESAMIENTO REACTIVO DE INVENTARIO
    // ================================
    @GetMapping("/inventory/process")
    public String processInventoryReactively() {
        System.out.println("\nPROCESAMIENTO REACTIVO DE INVENTARIO");

        Flux<String> inventoryFlux = reactiveProductService.processInventoryReactively()
                .doOnNext(item -> System.out.println("Inventario procesado: " + item))
                .doOnComplete(() -> System.out.println("Inventario completamente procesado"));

        ProductSubscriber inventorySubscriber = new ProductSubscriber(2);
        inventoryFlux.subscribe(inventorySubscriber);

        System.out.println("============================================================\n");
        return "Procesamiento de inventario iniciado. Revisa la consola.";
    }

    // ================================
    // ENDPOINTS REACTIVOS ESTÁNDAR
    // ================================
    @GetMapping("/orders/flux")
    public Flux<Map<String, Object>> getOrderFlux() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofMillis(500))
                .map(i -> {
                    double amount = ThreadLocalRandom.current().nextDouble(5.0, 50.0);
                    return Map.<String, Object>of(
                        "orderId", i,
                        "amount", Math.round(amount * 100.0) / 100.0,
                        "timestamp", LocalDateTime.now().toString(),
                        "status", "PROCESSING",
                        "customer", "Cliente-" + i
                    );
                })
                .doOnNext(order -> System.out.println("Pedido generado: " + order));
    }

    @GetMapping("/orders/{id}")
    public Mono<Map<String, Object>> getOrderById(@PathVariable Long id) {
        return Mono.just(Map.<String, Object>of(
            "orderId", id,
            "amount", 29.99,
            "timestamp", LocalDateTime.now().toString(),
            "status", "COMPLETED",
            "customer", "Cliente-" + id,
            "products", List.of("Camisa", "Pantalón")
        )).delayElement(Duration.ofMillis(200));
    }

    @PostMapping("/orders/process")
    public Mono<String> processOrdersReactive(@RequestBody Map<String, Object> request) {
        return Mono.fromCallable(() -> {
            System.out.println("Procesando pedido reactivo: " + request);

            // Simular procesamiento
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return "Pedido procesado exitosamente con ID: " + System.currentTimeMillis();
        }).delayElement(Duration.ofMillis(500));
    }

    // ================================
    // INTEGRACIÓN CON DOMINIO DE LA TIENDA
    // ================================
    @PostMapping("/customer-order")
    public Mono<String> processCustomerOrder(@RequestBody Map<String, Object> orderData) {
        String customerName = (String) orderData.get("customerName");
        @SuppressWarnings("unchecked")
        List<String> products = (List<String>) orderData.get("products");

        return reactiveProductService.processOrderReactively(customerName, products)
                .doOnSuccess(result -> System.out.println("Pedido completado: " + result));
    }

    // ================================
    // FLUJO COMBINADO CON BACKPRESSURE
    // ================================
    @GetMapping("/combined-flow")
    public String runCombinedReactiveFlow() {
        System.out.println("\nFLUJO REACTIVO COMBINADO");

        // Flujo de productos
        Flux<String> productFlow = reactiveProductService.getProductsReactively()
                .take(5)
                .doOnNext(product -> System.out.println("Producto: " + product));

        // Flujo de pedidos
        Flux<Double> orderFlow = Flux.just(10.0, 15.5, 22.0, 8.75, 30.0)
                .delayElements(Duration.ofMillis(600))
                .doOnNext(order -> System.out.println("Pedido: $" + order));

        // Combinar flujos
        Flux.zip(productFlow, orderFlow)
                .map(tuple -> {
                    String product = tuple.getT1();
                    Double amount = tuple.getT2();
                    return "Venta: " + product + " por $" + amount;
                })
                .subscribe(new ProductSubscriber(2));

        System.out.println("==============================================\n");
        return "Flujo combinado iniciado. Revisa la consola.";
    }

    // ================================
    // PROCESAMIENTO REACTIVO DE PEDIDOS REALES
    // ================================
    @GetMapping("/real-order/{id}")
    public String processRealOrder(@PathVariable Long id) {
        System.out.println("\nPROCESAMIENTO REACTIVO DE PEDIDO REAL");

        try {
            Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);

            if (pedidoOpt.isPresent()) {
                Pedido pedido = pedidoOpt.get();
                reactiveOrderService.procesarPedidoReal(pedido);
                return "Procesamiento reactivo del pedido #" + id + " iniciado. Revisa la consola para ver el flujo.";
            } else {
                return "Pedido #" + id + " no encontrado. Creando pedido de demostración...";
            }
        } catch (Exception e) {
            System.out.println("Error procesando pedido real: " + e.getMessage());
            return "Error al procesar pedido #" + id + ": " + e.getMessage();
        }
    }

    @GetMapping("/all-orders")
    public String processAllOrders() {
        System.out.println("\nPROCESAMIENTO REACTIVO DE TODOS LOS PEDIDOS");

        try {
            List<Pedido> pedidos = pedidoRepository.findAll();

            if (pedidos.isEmpty()) {
                return "No hay pedidos en la base de datos para procesar.";
            }

            System.out.println("Procesando " + pedidos.size() + " pedidos de forma reactiva...");

            // Procesar cada pedido de forma reactiva
            for (Pedido pedido : pedidos) {
                List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedido.getId());
                if (detalles != null && !detalles.isEmpty()) {
                    reactiveOrderService.procesarPedidoReal(pedido);
                } else {
                    System.out.println("Saltando pedido #" + pedido.getId() + " - Sin detalles");
                }
            }

            return "Procesamiento reactivo de " + pedidos.size() + " pedidos iniciado. Revisa la consola.";
        } catch (Exception e) {
            System.out.println("Error procesando pedidos: " + e.getMessage());
            return "Error al procesar pedidos: " + e.getMessage();
        }
    }
}
