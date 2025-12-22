package espe.edu.tienda_ropa.reactive;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Servicio reactivo especializado en:
 * - Detección de errores
 * - Clasificación de fallos
 * - Recuperación del flujo
 * - Mensajes orientados al usuario final
 */
@Service
public class ReactiveOrderRecoveryService {

    private static final double MAX_ALLOWED_AMOUNT = 20.00;

    /**
     * Procesa pedidos aplicando control avanzado de errores
     * y mensajes orientados a la corrección del usuario.
     */
    public Mono<String> handleOrderWithRecovery(double price, int quantity, int stock) {

        return Mono.fromCallable(() -> {

                    System.out.println(" Iniciando procesamiento del pedido");

                    // =====================
                    // VALIDACIONES TÉCNICAS
                    // =====================

                    if (price <= 0) {
                        throw new IllegalArgumentException(
                                "ERROR: Precio inválido | CAUSA: El precio ingresado es menor o igual a cero | " +
                                        "ACCIÓN: Ingrese un precio mayor a 0"
                        );
                    }

                    if (stock < 0) {
                        throw new IllegalStateException(
                                "ERROR: Stock negativo | CAUSA: El inventario no puede ser menor a cero | " +
                                        "ACCIÓN: Verifique el stock disponible en bodega"
                        );
                    }

                    if (quantity > stock) {
                        throw new IllegalStateException(
                                "ERROR: Stock insuficiente | CAUSA: Cantidad solicitada mayor al stock | " +
                                        "ACCIÓN: Reduzca la cantidad o espere reposición"
                        );
                    }

                    double total = price * quantity;

                    if (total > MAX_ALLOWED_AMOUNT) {
                        throw new ArithmeticException(
                                "ERROR: Pedido sospechoso | CAUSA: El monto supera el límite permitido | " +
                                        "ACCIÓN: Divida el pedido o contacte a soporte"
                        );
                    }

                    return "✔ Pedido aceptado correctamente | Total: $" + total;
                })

                // =====================
                // MANEJO CLASIFICADO
                // =====================

                .onErrorResume(IllegalArgumentException.class, error -> {
                    System.out.println(" Validación fallida");
                    return Mono.just(error.getMessage());
                })

                .onErrorResume(IllegalStateException.class, error -> {
                    System.out.println(" Error de inventario");
                    return Mono.just(error.getMessage());
                })

                .onErrorResume(ArithmeticException.class, error -> {
                    System.out.println(" Alerta de control financiero");
                    return Mono.just(error.getMessage());
                })

                // =====================
                // FALLBACK GENERAL
                // =====================

                .onErrorReturn(
                        "ERROR GENERAL | CAUSA: Falla no identificada | ACCIÓN: Intente nuevamente más tarde"
                )

                // =====================
                // LOGS REACTIVOS
                // =====================

                .doOnSubscribe(sub -> System.out.println(" Flujo reactivo iniciado"))
                .doFinally(signal -> System.out.println(" Flujo finalizado con señal: " + signal));
    }

    /**
     * Simulación técnica de un flujo reactivo con errores
     * para demostrar resiliencia y recuperación.
     */
    public Flux<Map<String, Object>> simulateOrderFlowWithRecovery() {

        return Flux.range(1, 6)
                .delayElements(Duration.ofMillis(400))

                .map(id -> {
                    double amount = ThreadLocalRandom.current().nextDouble(5, 35);

                    // Error intencional
                    if (amount > 25) {
                        throw new RuntimeException(
                                "Pedido #" + id + " rechazado: monto fuera de política"
                        );
                    }

                    return Map.<String, Object>of(
                            "orderId", id,
                            "amount", Math.round(amount * 100.0) / 100.0,
                            "status", "PROCESSED"
                    );
                })

                // Recuperación del flujo completo
                .onErrorResume(error -> {
                    System.out.println(" Error detectado en flujo: " + error.getMessage());

                    return Flux.just(
                            Map.of(
                                    "orderId", -1,
                                    "amount", 10.00,
                                    "status", "RECOVERED",
                                    "message", "Pedido alternativo generado automáticamente"
                            ),
                            Map.of(
                                    "orderId", -2,
                                    "amount", 12.00,
                                    "status", "FALLBACK",
                                    "message", "Pedido enviado a revisión manual"
                            )
                    );
                });
    }
}