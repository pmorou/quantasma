package quantasma.app.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quantasma.core.BarPeriod;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class FeedBarsSettings {
    private final String symbol;
    private final BarPeriod barPeriod;
    private final Instant fromDate;
    private final Instant toDate;
}
