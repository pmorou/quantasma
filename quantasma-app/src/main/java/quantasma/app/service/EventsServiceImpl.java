package quantasma.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quantasma.app.event.EventBuffer;
import quantasma.core.TradeEngine;
import quantasma.integrations.event.AccountEvent;
import quantasma.integrations.event.QuoteEvent;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class EventsServiceImpl implements EventsService {

    private final EventBuffer<QuoteEvent> quoteEventBuffer = new EventBuffer<>();
    private final EventBuffer<AccountEvent> accountEventBuffer = new EventBuffer<>();

    private final TradeEngine tradeEngine;

    @Autowired
    public EventsServiceImpl(TradeEngine tradeEngine) {
        this.tradeEngine = tradeEngine;
    }

    @Override
    public Flux<QuoteEvent> quotes() {
        return Flux.from(quoteEventBuffer.publisher());
    }

    public void publish(QuoteEvent quoteEvent) {
        tradeEngine.process(quoteEvent.data());
        quoteEventBuffer.setNext(quoteEvent);
    }

    @Override
    public Flux<AccountEvent> account() {
        return Flux.from(accountEventBuffer.publisher());
    }

    @Override
    public void publish(AccountEvent accountEvent) {
        accountEventBuffer.setNext(accountEvent);
    }

}
