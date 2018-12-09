package quantasma.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quantasma.app.event.SseEvent;
import quantasma.app.service.EventsService;
import quantasma.core.Quote;
import quantasma.integrations.event.AccountInfo;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("events")
public class EventsController {

    private final EventsService eventsService;

    @Autowired
    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @GetMapping(value = "quotes")
    public Flux<ServerSentEvent<Quote>> quotes() {
        return eventsService.quotes()
                            .map(quoteEvent -> SseEvent.create(quoteEvent).sse());
    }

    @GetMapping(value = "account")
    public Flux<ServerSentEvent<AccountInfo>> account() {
        return eventsService.account()
                            .map(accountEvent -> SseEvent.create(accountEvent).sse());
    }

}
