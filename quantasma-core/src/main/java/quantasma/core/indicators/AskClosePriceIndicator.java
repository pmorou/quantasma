package quantasma.core.indicators;

import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;
import quantasma.core.timeseries.TypedTimeSeries;
import quantasma.core.timeseries.bar.BidAskBar;

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
