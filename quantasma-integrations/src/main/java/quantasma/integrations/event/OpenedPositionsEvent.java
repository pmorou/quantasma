package quantasma.integrations.event;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class OpenedPositionsEvent implements Event<List<OpenedPosition>> {
    private static final String NAME = "opened_positions-event";

    private final String id;
    private final List<OpenedPosition> data;

    OpenedPositionsEvent(List<OpenedPosition> data) {
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
    public List<OpenedPosition> data() {
        return data;
    }
}
