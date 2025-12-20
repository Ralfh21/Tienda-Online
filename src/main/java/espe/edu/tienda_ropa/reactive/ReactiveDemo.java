package espe.edu.tienda_ropa.reactive;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Componente que ejecuta automáticamente las demostraciones reactivas
 * al inicializar la aplicación
 */
//@Component  // Temporalmente deshabilitado para evitar problemas al inicio
public class ReactiveDemo {

    @EventListener(ApplicationReadyEvent.class)
    public void runReactiveDemo() {
        System.out.println("\n");
        System.out.println("============================================================");
        System.out.println("          DEMOSTRACIÓN AUTOMÁTICA - ARQUITECTURA REACTIVA");
        System.out.println("============================================================");
        System.out.println("Iniciado en: " + LocalDateTime.now());
        System.out.println();

        // Esperar un poco antes de iniciar las demos
        Flux.just("Demo iniciando...")
            .delayElements(Duration.ofSeconds(2))
            .subscribe(msg -> {
                System.out.println(msg);
                runBasicReactiveDemo();
            });
    }

    private void runBasicReactiveDemo() {
        System.out.println("\n========== DEMO 1: FLUJO BÁSICO CON SUBSCRIBER ==========");

        // Crear flujo de datos simulado
        Flux<String> demoFlow = Flux.just(
            "Producto A", "Producto B", "Producto C",
            "Producto D", "Producto E", "Producto F"
        )
        .delayElements(Duration.ofMillis(500))
        .map(producto -> {
            System.out.println("Procesando: " + producto);
            return "Procesado: " + producto;
        })
        .doOnComplete(() -> {
            System.out.println("Demo básico completado");
            runAdvancedDemo();
        });

        // Usar nuestro subscriber personalizado
        ProductSubscriber subscriber = new ProductSubscriber(2);
        demoFlow.subscribe(subscriber);
    }

    private void runAdvancedDemo() {
        System.out.println("\n========== DEMO 2: FLUJO AVANZADO CON BACKPRESSURE ==========");

        Flux<Double> orderFlow = Flux.just(10.5, 25.0, 5.75, 45.0, 15.25, 60.0, 8.50)
            .delayElements(Duration.ofMillis(300))
            .filter(amount -> {
                boolean valid = amount >= 10.0;
                System.out.println("Pedido $" + amount + " - " + (valid ? "Válido" : "Rechazado"));
                return valid;
            })
            .map(amount -> {
                if (amount > 50.0) {
                    throw new RuntimeException("Monto muy alto: $" + amount);
                }
                double withTax = amount * 1.12;
                System.out.println("Impuestos aplicados: $" + amount + " -> $" +
                    Math.round(withTax * 100.0) / 100.0);
                return withTax;
            })
            .onErrorResume(error -> {
                System.out.println("Error manejado: " + error.getMessage());
                System.out.println("Aplicando valores de respaldo");
                return Flux.just(20.0, 25.0, 30.0);
            })
            .doOnComplete(() -> {
                System.out.println("Demo avanzado completado");
                showFinalMessage();
            });

        OrderSubscriber orderSubscriber = new OrderSubscriber(3);
        orderFlow.subscribe(orderSubscriber);
    }

    private void showFinalMessage() {
        Flux.just("Finalizando demostración...")
            .delayElements(Duration.ofSeconds(1))
            .subscribe(msg -> {
                System.out.println("\n============================================================");
                System.out.println("                    DEMOSTRACIÓN COMPLETADA");
                System.out.println("============================================================");
                System.out.println("Arquitectura reactiva implementada exitosamente");
                System.out.println("Subscriber personalizado funcionando con backpressure");
                System.out.println("Integración con dominio de tienda de ropa completada");
                System.out.println();
                System.out.println("Endpoints disponibles:");
                System.out.println("   GET  /reactive/orders/demo         - Demo básico");
                System.out.println("   GET  /reactive/products/demo       - Demo de productos");
                System.out.println("   GET  /reactive/inventory/process   - Procesamiento de inventario");
                System.out.println("   GET  /reactive/orders/flux         - Flujo de pedidos");
                System.out.println("   GET  /reactive/combined-flow       - Flujo combinado");
                System.out.println("   POST /reactive/customer-order      - Procesar pedido de cliente");
                System.out.println("============================================================\n");
            });
    }
}
