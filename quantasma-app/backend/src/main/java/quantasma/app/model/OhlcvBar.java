package quantasma.app.model;

import lombok.Data;
import quantasma.core.BarPeriod;

import java.time.Instant;

@Data
public class OhlcvBar {

    private final BarPeriod period;
    private final Instant date;
    private final String symbol;
    private final double bidOpen;
    private final double bidLow;
    private final double bidHigh;
    private final double bidClose;
    private final double askOpen;
    private final double askLow;
    private final double askHigh;
    private final double askClose;
    private final int volume;

}
