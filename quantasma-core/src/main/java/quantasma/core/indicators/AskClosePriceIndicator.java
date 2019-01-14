package quantasma.core.indicators;

import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;
import quantasma.core.timeseries.GenericTimeSeries;
import quantasma.core.timeseries.bar.BidAskBar;

public class AskClosePriceIndicator extends CachedIndicator<Num> {

    private final GenericTimeSeries<BidAskBar> timeSeries;

    public AskClosePriceIndicator(GenericTimeSeries<BidAskBar> timeSeries) {
        super(timeSeries.plainTimeSeries());
        this.timeSeries = timeSeries;
    }

    @Override
    protected Num calculate(int index) {
        return timeSeries.getBar(index).getAskClosePrice();
    }
}
