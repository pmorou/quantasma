package quantasma.app.model;

import lombok.Data;
import quantasma.core.BarPeriod;

import java.time.Instant;

@Data
public class HistoricalDataSummary {
    private final String symbol;
    private final BarPeriod period;
    private final Instant fromDate;
    private final Instant toDate;
    private final long barCount;
}
