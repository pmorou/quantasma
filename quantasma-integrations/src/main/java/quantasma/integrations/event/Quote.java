package quantasma.integrations.event;

import lombok.Data;

@Data
public class Quote {
    private final String symbol;
    private final double bid;
    private final double ask;
}
