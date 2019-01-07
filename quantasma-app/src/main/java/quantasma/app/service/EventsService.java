package quantasma.app.service;

import quantasma.integrations.event.AccountStateEvent;
import quantasma.integrations.event.OpenedPositionsEvent;
import quantasma.integrations.event.QuoteEvent;
import reactor.core.publisher.Flux;

public interface EventsService {

    Flux<QuoteEvent> quote();

    Flux<AccountStateEvent> accountState();

    Flux<OpenedPositionsEvent> openedPositions();

}
