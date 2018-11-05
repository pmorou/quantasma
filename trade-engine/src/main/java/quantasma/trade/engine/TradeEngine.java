package quantasma.trade.engine;

import java.time.ZonedDateTime;

public interface TradeEngine {
    void process(String symbol, ZonedDateTime date, double price);
}
