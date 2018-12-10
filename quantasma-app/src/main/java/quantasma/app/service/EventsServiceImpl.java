package quantasma.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quantasma.app.event.EventBuffer;
import quantasma.core.TradeEngine;
import quantasma.integrations.event.AccountStateEvent;
import quantasma.integrations.event.OpenedPositionsEvent;
import quantasma.integrations.event.QuoteEvent;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class EventsServiceImpl implements EventsService {

    private final EventBuffer<QuoteEvent> quoteEventBuffer = EventBuffer.instance(QuoteEvent.class);
    private final EventBuffer<AccountStateEvent> accountStateEventBuffer = EventBuffer.instance(AccountStateEvent.class);
    private final EventBuffer<OpenedPositionsEvent> openedPositionsEventBuffer = EventBuffer.instance(OpenedPositionsEvent.class);

    private final TradeEngine tradeEngine;

    @Autowired
    public EventsServiceImpl(TradeEngine tradeEngine) {
        this.tradeEngine = tradeEngine;
    }

    @Override
    public Flux<QuoteEvent> quote() {
        return Flux.from(quoteEventBuffer.publisher());
    }

    public void publish(QuoteEvent quoteEvent) {
        tradeEngine.process(quoteEvent.data());
        quoteEventBuffer.setNext(quoteEvent);
    }

    @Override
    public Flux<AccountStateEvent> accountState() {
        return Flux.from(accountStateEventBuffer.publisher());
    }

    @Override
    public void publish(AccountStateEvent accountStateEvent) {
        accountStateEventBuffer.setNext(accountStateEvent);
    }

    @Override
    public Flux<OpenedPositionsEvent> openedPositions() {
        return Flux.from(openedPositionsEventBuffer.publisher());
    }

    @Override
    public void publish(OpenedPositionsEvent openedPositionsEvent) {
        openedPositionsEventBuffer.setNext(openedPositionsEvent);
    }

}
