package quantasma.trade.engine.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import quantasma.model.CandlePeriod;
import quantasma.trade.engine.NaNBar;

public class AggregatedTimeSeries extends BaseTimeSeries {
    private final TimeSeries sourceTimeSeries;
    private final CandlePeriod candlePeriod;

    public AggregatedTimeSeries(TimeSeries sourceTimeSeries, String name, CandlePeriod candlePeriod) {
        super(name);
        this.sourceTimeSeries = sourceTimeSeries;
        this.candlePeriod = candlePeriod;
    }

    @Override
    public void addBar(Bar bar, boolean replace) {
        super.addBar(bar, replace);
    }

    @Override
    public Bar getFirstBar() {
        // avoid index manipulation
        return super.getBar(getBeginIndex());
    }

    @Override
    public Bar getLastBar() {
        // avoid index manipulation
        return super.getBar(getEndIndex());
    }

    @Override
    public Bar getBar(int index) {
        if (index == sourceTimeSeries.getEndIndex()) {
            return super.getBar(getEndIndex());
        }

        final int nthOldElement = sourceTimeSeries.getEndIndex() - index;

        if (nthOldElement < getBarCount()) {
            return super.getBar(getEndIndex() - nthOldElement);
        }

        return NaNBar.NaN;
    }
}
