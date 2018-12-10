package quantasma.app.service;

import quantasma.integrations.event.AccountStateEvent;
import quantasma.integrations.event.OpenedPositionsEvent;
import quantasma.integrations.event.QuoteEvent;
import reactor.core.publisher.Flux;

public interface EventsService {
    Flux<QuoteEvent> quote();

    void publish(QuoteEvent quoteEvent);

    Flux<AccountStateEvent> accountState();

    void publish(AccountStateEvent accountStateEvent);

    Flux<OpenedPositionsEvent> openedPositions();

    void publish(OpenedPositionsEvent openedPositionsEvent);
}
