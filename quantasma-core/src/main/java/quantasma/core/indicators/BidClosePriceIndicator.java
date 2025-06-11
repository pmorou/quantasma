package quantasma.core.indicators;

import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;
import quantasma.core.timeseries.GenericTimeSeries;
import quantasma.core.timeseries.bar.BidAskBar;

public class BidClosePriceIndicator extends CachedIndicator<Num> {

    private final GenericTimeSeries<BidAskBar> timeSeries;

    public BidClosePriceIndicator(GenericTimeSeries<BidAskBar> timeSeries) {
        super(timeSeries.plainTimeSeries());
        this.timeSeries = timeSeries;
    }

    @Override
    protected Num calculate(int index) {
        return timeSeries.getBar(index).getBidClosePrice();
    }

    @Override
    public int getCountOfUnstableBars() {
        return 0;
    }
}
