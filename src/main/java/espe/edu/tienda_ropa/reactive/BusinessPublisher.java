package espe.edu.tienda_ropa.reactive;
// ================= PUBLISHER REACTIVO PARA LÓGICA DE NEGOCIO =================
//
// Implementación de Publisher para productos, pedidos y montos del carrito
// en una arquitectura reactiva de tienda online.

import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class BusinessPublisher {
    public static void main(String[] args) throws InterruptedException {
        // Publisher de productos
        List<String> productos = Arrays.asList("Camiseta", "Pantalón", "Zapatos");
        Flux<String> productosFlux = Flux.fromIterable(productos)
            .filter(p -> !p.equals("Zapatos")) // Solo productos disponibles
            .map(p -> p + " (IVA incluido)")
            .delayElements(Duration.ofMillis(500)); // Simular asincronía

        productosFlux.subscribe(System.out::println);

        // Publisher de pedidos
        List<Integer> pedidos = Arrays.asList(101, 0, 102);
        Flux<Integer> pedidosFlux = Flux.fromIterable(pedidos)
            .filter(id -> id > 0) // Solo pedidos válidos
            .delayElements(Duration.ofMillis(400));

        pedidosFlux.subscribe(id -> System.out.println("Pedido: " + id));

        // Publisher de montos del carrito
        List<Double> montos = Arrays.asList(10.0, 50.0, 5.0);
        Flux<Double> montosFlux = Flux.fromIterable(montos)
            .filter(m -> m >= 20.0)
            .map(m -> m * 1.12) // Calcular total con IVA (12%)
            .delayElements(Duration.ofMillis(300));

        montosFlux.subscribe(m -> System.out.println("Monto con IVA: $" + String.format("%.2f", m)));

        // Esperar a que terminen los flujos (solo para demo)
        Thread.sleep(2000);
    }
}

/*
==================== Justificación académica ====================
Este fragmento implementa el patrón Publisher usando Project Reactor (Flux) para simular flujos de datos de productos, pedidos y montos del carrito en una tienda online.

Se aplican transformaciones como filter (para filtrar productos disponibles y pedidos válidos) y map (para calcular precios con IVA), modelando la lógica de negocio real.

El uso de delayElements simula asincronía, representando el procesamiento no bloqueante típico de sistemas reactivos modernos.

Esta implementación permite transformar y procesar eventos del dominio de manera eficiente y declarativa, cumpliendo con los principios de la arquitectura reactiva.
*/
