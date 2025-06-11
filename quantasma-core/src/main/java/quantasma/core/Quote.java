package quantasma.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.ta4j.core.num.DecimalNumFactory;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

import java.time.Instant;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Quote {
    private final String symbol;
    private final Instant time;
    private final Num bid;
    private final Num ask;

    public static Quote price(String symbol, Instant time, Num price) {
        return new Quote(symbol, time, price, NaN.NaN);
    }

    public static Quote bidAsk(String symbol, Instant time, double bid, double ask) {
        return new Quote(symbol, time, DecimalNumFactory.getInstance().numOf(bid), DecimalNumFactory.getInstance().numOf(ask));
    }

    public static Quote bidAsk(String symbol, Instant time, Num bid, Num ask) {
        return new Quote(symbol, time, bid, ask);
    }
}
