package quantasma.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Quote {
    private final String symbol;
    private final ZonedDateTime time;
    private final double bid;
    private final double ask;

    public static Quote price(String symbol, ZonedDateTime time, double price) {
        return new Quote(symbol, time, price, 0.0d);
    }

    public static Quote bidAsk(String symbol, ZonedDateTime time, double bid, double ask) {
        return new Quote(symbol, time, bid, ask);
    }

}
