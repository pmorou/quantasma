package quantasma.core.indicators;

import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;
import quantasma.core.BidAskBar;
import quantasma.core.timeseries.TypedTimeSeries;

public class BidClosePriceIndicator extends CachedIndicator<Num> {

    private final TypedTimeSeries<BidAskBar> typedTimeSeries;

    public BidClosePriceIndicator(TypedTimeSeries<BidAskBar> typedTimeSeries) {
        super(typedTimeSeries.getTimeSeries());
        this.typedTimeSeries = typedTimeSeries;
    }

    @Override
    protected Num calculate(int index) {
        return typedTimeSeries.getBar(index).getBidClosePrice();
    }
}
