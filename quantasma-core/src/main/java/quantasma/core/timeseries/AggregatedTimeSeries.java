package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.NaNBar;

public class AggregatedTimeSeries extends BaseTimeSeries {
    private final TimeSeries sourceTimeSeries;
    private final BarPeriod barPeriod;

    public AggregatedTimeSeries(TimeSeries sourceTimeSeries, String name, BarPeriod barPeriod) {
        super(name);
        this.sourceTimeSeries = sourceTimeSeries;
        this.barPeriod = barPeriod;
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
