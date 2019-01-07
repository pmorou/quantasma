package quantasma.app.event;

import quantasma.integrations.event.Event;
import quantasma.integrations.event.EventPublisher;
import reactor.adapter.JdkFlowAdapter;
import reactor.core.publisher.Flux;

public class FluxPublisher {

    @SuppressWarnings("uncheck")
    public static <T extends Event> Flux<T> from(EventPublisher eventPublisher, Class<T> eventClass) {
        return (Flux<T>) JdkFlowAdapter.flowPublisherToFlux(eventPublisher)
                                       .filter(eventClass::isInstance);
    }
}
