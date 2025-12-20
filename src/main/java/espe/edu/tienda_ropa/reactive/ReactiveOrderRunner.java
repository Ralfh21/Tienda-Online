package espe.edu.tienda_ropa.reactive;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class ReactiveOrderRunner {

    public static void main(String[] args) {

        Flux<Double> pedidos = Flux.just(2.50, 5.00, 12.00, 1.00, 25.00, 8.50)
                .delayElements(Duration.ofMillis(400))   // asincronía
                .filter(p -> p >= 5.00)                  // filtro
                .map(p -> {
                    if (p > 20.00) {
                        throw new RuntimeException("Pedido inválido (>20.00): " + p);
                    }
                    return p;
                })
                // ELIGE SOLO UNA:
                .map(p -> p * 1.12)      // IVA 12%
                // .map(p -> p + 0.50)   // Recargo por servicio
                .onErrorResume(err -> {
                    System.out.println("Recuperación activa: " + err.getMessage());
                    return Flux.just(6.00, 7.00, 8.00);
                });

        OrderSubscriber subscriber = new OrderSubscriber(2);
        pedidos.subscribe(subscriber);

        subscriber.await();
    }
}
