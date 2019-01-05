package quantasma.app.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import quantasma.integrations.event.Event;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class EventBuffer<E extends Event> {
    private final BlockingQueue<E> buffer = new ArrayBlockingQueue<>(1);
    private final Class<E> typeHolder;

    private EventBuffer(Class<E> clazz) {
        this.typeHolder = clazz;
    }

    public static <E extends Event> EventBuffer<E> instance(Class<E> clazz) {
        return new EventBuffer<>(clazz);
    }

    public EventPublisher<E> publisher() {
        log.info("Publisher of [{}] created", typeHolder.getName());
        return new EventPublisher<>(buffer);
    }

    public boolean setNext(E quoteEvent) {
        try {
            final boolean inserted = buffer.offer(quoteEvent, 1, TimeUnit.SECONDS);
            if (!inserted) {
                // remove previous value to make room for the up to date value
                buffer.take();
            }
            return inserted;
        } catch (InterruptedException e) {
            log.error("Offering interrupted", e);
        }
        return false;
    }

    @AllArgsConstructor
    private class EventPublisher<E> implements Publisher<E> {
        private final BlockingQueue<E> events;

        @Override
        public void subscribe(Subscriber<? super E> subscriber) {
            log.info("New subscription of [{}] registered", typeHolder.getName());
            subscriber.onSubscribe(new EventSubscription<>(events, subscriber));
        }
    }

    @AllArgsConstructor
    private class EventSubscription<E> implements Subscription {
        private final BlockingQueue<E> events;
        private final Subscriber<? super E> subscriber;
        private final AtomicBoolean closed = new AtomicBoolean(false);

        @Override
        public void request(long n) {
            if (n <= 0) {
                subscriber.onError(new IllegalArgumentException());
            }

            if (closed.get()) {
                subscriber.onError(new IllegalAccessException());
            }

            try {
                subscriber.onNext(events.take());
            } catch (InterruptedException e) {
                log.error(String.format("Subscription of [%s] encountered exception while request", typeHolder.getName()), e);
            }
        }

        @Override
        public void cancel() {
            log.info("Subscription of [{}] canceled", typeHolder.getName());
            closed.set(true);
        }
    }
}
