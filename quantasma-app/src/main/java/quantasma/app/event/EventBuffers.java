package quantasma.app.event;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import quantasma.integrations.event.Event;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EventBuffers {
    private final Map<Class<?>, EventBuffer<?>> eventBuffers = new HashMap<>();

    public <E extends Event> Publisher<E> publisherFor(Class<E> clazz) {
        return getBuffer(clazz).publisher();
    }

    public <E extends Event> void next(E event) {
        registerBufferIfAbsent(event.getClass());
        getBuffer(event.getClass()).setNext(event);
    }

    private <E extends Event> void registerBufferIfAbsent(Class<E> eventClass) {
        eventBuffers.putIfAbsent(eventClass, EventBuffer.instance(eventClass));
    }

    private <E extends Event<E>> EventBuffer<E> getBuffer(Class<E> clazz) {
        return (EventBuffer<E>) eventBuffers.get(clazz);
    }
}