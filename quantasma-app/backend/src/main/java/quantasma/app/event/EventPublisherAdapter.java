package quantasma.app.event;

import quantasma.integrations.event.Event;
import quantasma.integrations.event.EventPublisher;
import reactor.adapter.JdkFlowAdapter;
import reactor.core.publisher.Flux;

public final class EventPublisherAdapter {
    private EventPublisherAdapter() {
    }

    @SuppressWarnings("uncheck")
    public static <T extends Event> Flux<T> toFlux(EventPublisher eventPublisher, Class<T> filterEvents) {
        return (Flux<T>) JdkFlowAdapter.flowPublisherToFlux(eventPublisher)
            .filter(filterEvents::isInstance);
    }
}
