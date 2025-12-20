package espe.edu.tienda_ropa.reactive;

import espe.edu.tienda_ropa.domain.Producto;
import espe.edu.tienda_ropa.repository.ProductoDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Servicio reactivo para el manejo de productos
 * Integra el dominio existente con la arquitectura reactiva
 */
@Service
public class ReactiveProductService {

    @Autowired
    private ProductoDomainRepository productoRepository;

    /**
     * Obtiene productos de forma reactiva con delay simulado
     */
    public Flux<String> getProductsReactively() {
        return Flux.fromIterable(getProductNames())
                .delayElements(Duration.ofMillis(300))
                .map(name -> {
                    System.out.println("Procesando producto: " + name);
                    return name;
                })
                .filter(name -> name.length() > 3)
                .onErrorResume(error -> {
                    System.out.println("Error procesando productos: " + error.getMessage());
                    return Flux.just("Producto Genérico", "Producto de Respaldo");
                });
    }

    /**
     * Simula el procesamiento de inventario de forma reactiva
     */
    public Flux<String> processInventoryReactively() {
        return Flux.just(
                "Camisa Formal", "Pantalón Jeans", "Vestido Elegante",
                "Chaqueta de Cuero", "Zapatillas Deportivas", "Blusa Casual",
                "Falda Plisada", "Sombrero", "Cinturón de Cuero", "Bufanda"
            )
            .delayElements(Duration.ofMillis(400))
            .map(product -> {
                // Simular validación de inventario
                int stock = ThreadLocalRandom.current().nextInt(0, 100);
                String status = stock > 10 ? "EN STOCK" : "STOCK BAJO";
                return product + " - " + status + " (" + stock + " unidades)";
            })
            .filter(productInfo -> !productInfo.contains("STOCK BAJO"))
            .doOnNext(product -> System.out.println("Inventario validado: " + product));
    }

    /**
     * Procesa pedidos de forma reactiva
     */
    public Mono<String> processOrderReactively(String customerName, List<String> products) {
        return Mono.fromCallable(() -> {
            System.out.println("Procesando pedido para: " + customerName);
            System.out.println("Productos en el pedido: " + products);

            double total = products.size() * ThreadLocalRandom.current().nextDouble(15.0, 50.0);
            total = Math.round(total * 100.0) / 100.0;

            return "Pedido procesado - Cliente: " + customerName +
                   ", Total: $" + total + ", Productos: " + products.size();
        })
        .delayElement(Duration.ofMillis(800)) // Usar delay reactivo en lugar de Thread.sleep
        .onErrorReturn("Error procesando el pedido");
    }

    /**
     * Obtiene nombres de productos desde la base de datos o valores por defecto
     */
    private List<String> getProductNames() {
        try {
            List<Producto> productos = productoRepository.findAll();
            if (!productos.isEmpty()) {
                return productos.stream()
                    .map(Producto::getNombre)
                    .limit(8)
                    .toList();
            }
        } catch (Exception e) {
            System.out.println("No se pudieron cargar productos de BD, usando valores por defecto");
        }

        // Valores por defecto si no hay productos en BD
        return List.of(
            "Camisa Clásica", "Pantalón Formal", "Vestido de Noche",
            "Chaqueta Deportiva", "Zapatillas Casuales", "Blusa Elegante",
            "Falda Corta", "Sombrero de Verano"
        );
    }
}
