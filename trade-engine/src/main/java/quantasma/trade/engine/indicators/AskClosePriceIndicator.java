package quantasma.trade.engine.indicators;

import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;
import quantasma.trade.engine.BidAskBar;
import quantasma.trade.engine.rule.TypedTimeSeries;

public class AskClosePriceIndicator extends CachedIndicator<Num> {

    private final TypedTimeSeries<BidAskBar> typedTimeSeries;

    public AskClosePriceIndicator(TypedTimeSeries<BidAskBar> typedTimeSeries) {
        super(typedTimeSeries.getTimeSeries());
        this.typedTimeSeries = typedTimeSeries;
    }

    @Override
    protected Num calculate(int index) {
        return typedTimeSeries.getBar(index).getAskClosePrice();
    }
}
