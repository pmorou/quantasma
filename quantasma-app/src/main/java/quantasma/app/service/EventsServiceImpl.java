package quantasma.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quantasma.app.event.FluxPublisher;
import quantasma.integrations.event.AccountStateEvent;
import quantasma.integrations.event.EventPublisher;
import quantasma.integrations.event.OpenedPositionsEvent;
import quantasma.integrations.event.QuoteEvent;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class EventsServiceImpl implements EventsService {

    private final EventPublisher eventPublisher;

    @Autowired
    public EventsServiceImpl(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Flux<QuoteEvent> quote() {
        return FluxPublisher.from(eventPublisher, QuoteEvent.class);
    }

    @Override
    public Flux<AccountStateEvent> accountState() {
        return FluxPublisher.from(eventPublisher, AccountStateEvent.class);
    }

    @Override
    public Flux<OpenedPositionsEvent> openedPositions() {
        return FluxPublisher.from(eventPublisher, OpenedPositionsEvent.class);
    }

}
