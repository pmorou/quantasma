package quantasma.integrations.event;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
class WorkingEventSink implements EventSink {

    private final DefaultEventPipe defaultEventHandler = new DefaultEventPipe();
    private final Map<Class<?>, EventPipe> handlers = new HashMap<>();

    WorkingEventSink() {
    }

    @Override
    public void flush(Event<?> event) {
        handlers.getOrDefault(event.getClass(), defaultEventHandler).handle(event);
    }

    @Override
    public <E extends Event<D>, D> EventSink install(Class<E> type, EventPipe<E, D> eventHandler) {
        handlers.put(type, eventHandler);
        return this;
    }

    private static class DefaultEventPipe implements EventPipe {
        @Override
        public void handle(Event event) {
            log.warn("Unhandled event [{}]", event.getClass());
        }
    }
}
