package espe.edu.tienda_ropa.reactive;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CountDownLatch;

public class OrderSubscriber implements Subscriber<Double> {

    private final int batchSize;
    private Subscription subscription;
    private int processedInBatch = 0;
    private final CountDownLatch done = new CountDownLatch(1);

    public OrderSubscriber(int batchSize) {
        this.batchSize = batchSize;
    }

    public void await() {
        try {
            done.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        System.out.println("onSubscribe: suscripción iniciada");
        System.out.println("Backpressure -> solicitando " + batchSize + " pedidos");
        subscription.request(batchSize);
    }

    @Override
    public void onNext(Double value) {
        processedInBatch++;
        System.out.println("onNext: pedido procesado = " + value);

        if (processedInBatch == batchSize) {
            processedInBatch = 0;
            System.out.println("Lote procesado -> solicitando " + batchSize + " más");
            subscription.request(batchSize);
        }
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("onError: " + t.getMessage());
        done.countDown();
    }

    @Override
    public void onComplete() {
        System.out.println("onComplete: flujo de pedidos finalizado");
        done.countDown();
    }
}
