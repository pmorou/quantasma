package quantasma.core;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Quote {
    private final String symbol;
    private final ZonedDateTime time;
    private final double bid;
    private final double ask;
}
