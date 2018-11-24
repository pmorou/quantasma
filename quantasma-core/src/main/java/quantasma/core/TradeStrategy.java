package quantasma.core;

import org.ta4j.core.Strategy;
import org.ta4j.core.num.Num;

public interface TradeStrategy extends Strategy {
    Num getAmount();

    String getTradeSymbol();
}
