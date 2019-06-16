package quantasma.app.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import quantasma.integrations.event.Event;

import java.util.function.Function;

public interface SseEvent<D> extends Event<D> {

    static <D> SseEvent<D> create(Event<D> event) {
        return new EventWrapper<>(event);
    }

    default ServerSentEvent<D> sse() {
        return ServerSentEvent.<D>builder()
            .id(id())
            .event(name())
            .data(data())
            .build();
    }

    static <D> Function<Event<D>, ServerSentEvent<D>> buildSse() {
        return event -> SseEvent.create(event).sse();
    }

    @RequiredArgsConstructor
    class EventWrapper<D> implements SseEvent<D> {
        @Getter
        private final Event<D> event;

        @Override
        public String id() {
            return event.id();
        }

        @Override
        public String name() {
            return event.name();
        }

        @Override
        public D data() {
            return event.data();
        }
    }

}
