package quantasma.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quantasma.app.event.SseEvent;
import quantasma.app.service.EventsService;
import quantasma.core.Quote;
import quantasma.integrations.event.AccountState;
import quantasma.integrations.event.OpenedPosition;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("api/events")
public class EventsController {

    private final EventsService eventsService;

    @Autowired
    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @GetMapping("quote")
    public Flux<ServerSentEvent<Quote>> quote() {
        return eventsService.quote()
            .map(SseEvent.buildSse());
    }

    @GetMapping("accountState")
    public Flux<ServerSentEvent<AccountState>> accountState() {
        return eventsService.accountState()
            .map(SseEvent.buildSse());
    }

    @GetMapping("openedPositions")
    public Flux<ServerSentEvent<List<OpenedPosition>>> openedPositions() {
        return eventsService.openedPositions()
            .map(SseEvent.buildSse());
    }
}
