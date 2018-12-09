package quantasma.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import quantasma.app.event.EventBuffer;
import quantasma.integrations.event.QuoteEvent;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class EventsServiceImpl implements EventsService {

    private final EventBuffer<QuoteEvent> quoteEventBuffer = new EventBuffer<>();

    @Override
    public Flux<QuoteEvent> quotes() {
        return Flux.from(quoteEventBuffer.publisher());
    }

    public void publish(QuoteEvent quoteEvent) {
        quoteEventBuffer.setNext(quoteEvent);
    }

}
