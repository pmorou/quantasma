package quantasma.core.timeseries;

import lombok.AccessLevel;
import lombok.Getter;
import org.ta4j.core.Bar;
import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.NaNBar;

public class BaseAggregatedTimeSeries extends BaseSymbolTimeSeries implements AggregatedTimeSeries {
    @Getter
    private final MainTimeSeries mainTimeSeries;

    protected BaseAggregatedTimeSeries(MainTimeSeries mainTimeSeries, String name, String symbol, BarPeriod barPeriod) {
        super(name, symbol, barPeriod);
        this.mainTimeSeries = mainTimeSeries;
    }

    protected BaseAggregatedTimeSeries(MainTimeSeries mainTimeSeries, String name, String symbol, BarPeriod barPeriod, int maxBarCount) {
        super(name, symbol, barPeriod, maxBarCount);
        this.mainTimeSeries = mainTimeSeries;
    }

    protected BaseAggregatedTimeSeries(Builder builder) {
        super(builder);
        this.mainTimeSeries = builder.mainTimeSeries;
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
        if (index == mainTimeSeries.getEndIndex()) {
            return getLastBar();
        }

        final int nthOldElement = mainTimeSeries.getEndIndex() - index;

        if (nthOldElement < getBarCount()) {
            return super.getBar(getEndIndex() - nthOldElement);
        }

        return NaNBar.NaN;
    }

    @Getter(value = AccessLevel.PROTECTED)
    public static final class Builder extends BaseSymbolTimeSeries.Builder {
        private final MainTimeSeries mainTimeSeries;

        public Builder(String symbol, BarPeriod barPeriod, MainTimeSeries mainTimeSeries) {
            super(symbol, barPeriod);
            this.mainTimeSeries = mainTimeSeries;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public BaseAggregatedTimeSeries build() { return new BaseAggregatedTimeSeries(this); }
    }

}
