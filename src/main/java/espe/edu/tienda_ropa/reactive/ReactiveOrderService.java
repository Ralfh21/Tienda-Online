package espe.edu.tienda_ropa.reactive;

import espe.edu.tienda_ropa.domain.DetallePedido;
import espe.edu.tienda_ropa.domain.Pedido;
import espe.edu.tienda_ropa.repository.DetallePedidoDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
public class ReactiveOrderService {

    @Autowired
    private DetallePedidoDomainRepository detallePedidoRepository;

    public void procesarPedidoReal(Pedido pedido) {

        System.out.println("================================================");
        System.out.println("INICIANDO FLUJO REACTIVO REAL DEL PEDIDO #" + pedido.getId());
        System.out.println("Cliente ID: " + pedido.getClienteId());
        System.out.println("Total del pedido: $" + pedido.getTotal());

        // Obtener detalles del pedido desde el repositorio
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedido.getId());
        System.out.println("Total detalles: " + detalles.size());
        System.out.println("================================================");

        // Validar que el pedido tenga detalles
        if (detalles == null || detalles.isEmpty()) {
            System.out.println("ERROR: El pedido no tiene detalles para procesar");
            return;
        }

        Flux<Double> flujoPedidos = Flux.fromIterable(detalles)
                // Obtener el subtotal de cada detalle
                .map(detalle -> {
                    Double subtotal = detalle.getSubtotal() != null ? detalle.getSubtotal().doubleValue() : 0.0;
                    System.out.println("Procesando detalle - Producto ID: " +
                        detalle.getProductoId() +
                        ", Cantidad: " + detalle.getCantidad() +
                        ", Subtotal: $" + subtotal);
                    return subtotal;
                })

                // Simular asincronía (1 pedido cada 400 ms)
                .delayElements(Duration.ofMillis(400))

                // Filtrar pedidos válidos (>= 5.00)
                .filter(monto -> {
                    boolean esValido = monto >= 5.0;
                    System.out.println("Filtro: evaluando pedido $" + monto + " - " +
                        (esValido ? "VALIDO" : "RECHAZADO"));
                    return esValido;
                })

                // Transformación: aplicar IVA del 12%
                .map(monto -> {
                    if (monto > 500.0) {  // Cambié el límite a un valor más realista
                        System.out.println("ADVERTENCIA: Pedido de monto muy alto detectado: $" + monto);
                        throw new RuntimeException("Pedido inválido detectado: $" + monto);
                    }
                    double totalConIva = Math.round((monto * 1.12) * 100.0) / 100.0;  // Redondear a 2 decimales
                    System.out.println("Aplicando IVA: $" + monto + " -> $" + totalConIva);
                    return totalConIva;
                })

                // Manejo y recuperación de errores
                .onErrorResume(error -> {
                    System.out.println("ERROR: " + error.getMessage());
                    System.out.println("Aplicando valores de respaldo...");
                    return Flux.just(6.72, 7.84, 8.96); // Valores con IVA aplicado
                })

                .doOnComplete(() -> {
                    System.out.println("================================================");
                    System.out.println("FLUJO REACTIVO COMPLETADO PARA PEDIDO #" + pedido.getId());
                    System.out.println("================================================");
                });

        // Suscripción con Subscriber personalizado (backpressure)
        OrderSubscriber subscriber = new OrderSubscriber(2);
        flujoPedidos.subscribe(subscriber);

        // Opcional: Esperar a que termine el procesamiento
        try {
            subscriber.await();
            System.out.println("Procesamiento del pedido #" + pedido.getId() + " finalizado");
        } catch (Exception e) {
            System.out.println("Error esperando el procesamiento: " + e.getMessage());
        }
    }
}
