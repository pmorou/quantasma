package quantasma.app.event;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EventBuffer<E> {
    private final BlockingQueue<E> buffer = new ArrayBlockingQueue<>(1);

    public static EventBuffer instance() {
        return new EventBuffer<>();
    }

    public EventPublisher<E> publisher() {
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

    private static class EventPublisher<E> implements Publisher<E> {
        private final BlockingQueue<E> events;

        EventPublisher(BlockingQueue<E> events) {
            this.events = events;
        }

        @Override
        public void subscribe(Subscriber<? super E> s) {
            log.info("New subscription registered");

            class EventSubscription implements Subscription {
                @Override
                public void request(long n) {
                    try {
                        log.info("requesting");
                        s.onNext(events.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void cancel() {
                    log.info("Subscription canceled");
                }
            }

            s.onSubscribe(new EventSubscription());
        }
    }
}