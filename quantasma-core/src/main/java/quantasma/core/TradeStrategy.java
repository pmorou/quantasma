package quantasma.core;

import org.ta4j.core.Strategy;
import org.ta4j.core.num.Num;
import quantasma.core.analysis.parametrize.Parameterizable;
import quantasma.core.analysis.parametrize.Values;

public interface TradeStrategy extends Strategy {
    Num getAmount();

    String getTradeSymbol();

    Values<?> getParameterValues();

    Parameterizable[] parameterizables();
}
