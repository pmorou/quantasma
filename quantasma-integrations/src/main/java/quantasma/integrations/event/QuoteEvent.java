package quantasma.integrations.event;

import quantasma.core.Quote;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public class QuoteEvent implements Event<Quote> {
    private static final String NAME = "quote-event";

    private final String id;
    private final Quote data;

    QuoteEvent(Quote data) {
        this.data = data;
        this.id = ZonedDateTime.now(ZoneOffset.UTC).toString() + "_" + UUID.randomUUID().toString();
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Quote data() {
        return data;
    }
}
