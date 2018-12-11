package quantasma.core;

import org.ta4j.core.Strategy;
import org.ta4j.core.num.Num;
import quantasma.core.analysis.parametrize.Parameters;

public interface TradeStrategy extends Strategy {
    Num getAmount();

    String getTradeSymbol();

    Parameters getParameters();
}
