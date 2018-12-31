package quantasma.app.service;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quantasma.app.event.EventBuffer;
import quantasma.core.TradeEngine;
import quantasma.integrations.event.AccountStateEvent;
import quantasma.integrations.event.Event;
import quantasma.integrations.event.OpenedPositionsEvent;
import quantasma.integrations.event.QuoteEvent;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EventsServiceImpl implements EventsService {

    private final Map<Class<?>, EventBuffer<?>> eventBuffers = new HashMap<>();
    private final TradeEngine tradeEngine;

    @Autowired
    public EventsServiceImpl(TradeEngine tradeEngine) {
        this.tradeEngine = tradeEngine;
    }

    @Override
    public Flux<QuoteEvent> quote() {
        return Flux.from(publisherFor(QuoteEvent.class));
    }

    public void publish(QuoteEvent quoteEvent) {
        tradeEngine.process(quoteEvent.data());
        next(quoteEvent);
    }

    @Override
    public Flux<AccountStateEvent> accountState() {
        return Flux.from(publisherFor(AccountStateEvent.class));
    }

    @Override
    public void publish(AccountStateEvent accountStateEvent) {
        next(accountStateEvent);
    }

    @Override
    public Flux<OpenedPositionsEvent> openedPositions() {
        return Flux.from(publisherFor(OpenedPositionsEvent.class));
    }

    @Override
    public void publish(OpenedPositionsEvent openedPositionsEvent) {
        next(openedPositionsEvent);
    }

    private <E extends Event> Publisher<E> publisherFor(Class<E> clazz) {
        return (Publisher<E>) eventBuffers.get(clazz).publisher();
    }

    private <E extends Event> void next(E event) {
        eventBuffers.putIfAbsent(event.getClass(), EventBuffer.instance(event.getClass()));
        ((EventBuffer<E>) eventBuffers.get(event.getClass())).setNext(event);
    }
}
