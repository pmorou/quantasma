package quantasma.core;

import java.time.ZonedDateTime;

public interface TradeEngine {
    void process(String symbol, ZonedDateTime date, double price);

    void process(String symbol, ZonedDateTime date, double bid, double ask);

    void process(Quote quote);
}
