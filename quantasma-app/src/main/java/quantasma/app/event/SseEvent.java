package quantasma.app.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.codec.ServerSentEvent;
import quantasma.integrations.event.Event;

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

    @AllArgsConstructor
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
