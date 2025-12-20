package espe.edu.tienda_ropa.reactive;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

/**
 * Subscriber personalizado para el procesamiento reactivo de productos
 * Implementa backpressure y manejo de eventos del flujo reactivo
 */
public class ProductSubscriber implements Subscriber<String> {

    private final int batchSize;
    private Subscription subscription;
    private int processedCount = 0;
    private int batchCount = 0;
    private final CountDownLatch completionLatch = new CountDownLatch(1);

    public ProductSubscriber(int batchSize) {
        this.batchSize = batchSize;
    }

    public void awaitCompletion() {
        try {
            completionLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        System.out.println("[" + LocalDateTime.now() + "] ProductSubscriber suscrito");
        System.out.println("Backpressure: solicitando " + batchSize + " productos iniciales");
        subscription.request(batchSize);
    }

    @Override
    public void onNext(String product) {
        processedCount++;
        batchCount++;

        System.out.println("[" + processedCount + "] Producto procesado: " + product);

        // Simular procesamiento del producto
        try {
            Thread.sleep(100); // Simular tiempo de procesamiento
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Aplicar backpressure - solicitar más cuando el lote esté completo
        if (batchCount == batchSize) {
            batchCount = 0;
            System.out.println("Lote completado (" + batchSize + " productos)");
            System.out.println("Solicitando siguiente lote de " + batchSize + " productos");
            subscription.request(batchSize);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("[" + LocalDateTime.now() + "] Error en ProductSubscriber: " + throwable.getMessage());
        throwable.printStackTrace();
        completionLatch.countDown();
    }

    @Override
    public void onComplete() {
        System.out.println("[" + LocalDateTime.now() + "] ProductSubscriber completado");
        System.out.println("Total de productos procesados: " + processedCount);
        completionLatch.countDown();
    }

    public int getProcessedCount() {
        return processedCount;
    }
}
