package quantasma.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import quantasma.core.BarPeriod;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class FeedBarsSettings {
    private final String symbol;
    private final BarPeriod barPeriod;
    private final Instant fromDate;
    private final Instant toDate;
}
