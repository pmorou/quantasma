package quantasma.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quantasma.app.event.EventBuffers;
import quantasma.core.TradeEngine;
import quantasma.integrations.event.AccountStateEvent;
import quantasma.integrations.event.OpenedPositionsEvent;
import quantasma.integrations.event.QuoteEvent;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class EventsServiceImpl implements EventsService {

    private final EventBuffers eventBuffers = new EventBuffers();
    private final TradeEngine tradeEngine;

    @Autowired
    public EventsServiceImpl(TradeEngine tradeEngine) {
        this.tradeEngine = tradeEngine;
    }

    @Override
    public Flux<QuoteEvent> quote() {
        return Flux.from(eventBuffers.publisherFor(QuoteEvent.class));
    }

    public void publish(QuoteEvent quoteEvent) {
        tradeEngine.process(quoteEvent.data());
        eventBuffers.next(quoteEvent);
    }

    @Override
    public Flux<AccountStateEvent> accountState() {
        return Flux.from(eventBuffers.publisherFor(AccountStateEvent.class));
    }

    @Override
    public void publish(AccountStateEvent accountStateEvent) {
        eventBuffers.next(accountStateEvent);
    }

    @Override
    public Flux<OpenedPositionsEvent> openedPositions() {
        return Flux.from(eventBuffers.publisherFor(OpenedPositionsEvent.class));
    }

    @Override
    public void publish(OpenedPositionsEvent openedPositionsEvent) {
        eventBuffers.next(openedPositionsEvent);
    }

}
