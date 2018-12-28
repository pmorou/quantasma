package quantasma.core.timeseries;

import lombok.AccessLevel;
import lombok.Getter;
import org.ta4j.core.Bar;
import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.BidAskBar;

public class BaseAggregatedTimeSeries extends BaseSymbolTimeSeries implements AggregatedTimeSeries {
    @Getter
    private final MainTimeSeries mainTimeSeries;

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

        return BidAskBar.NaN;
    }

    public static class Builder<T extends Builder<T, R>, R extends BaseAggregatedTimeSeries> extends BaseSymbolTimeSeries.Builder<T, R> {
        @Getter(value = AccessLevel.PROTECTED)
        private final MainTimeSeries mainTimeSeries;

        public Builder(String symbol, BarPeriod barPeriod, MainTimeSeries mainTimeSeries) {
            super(symbol, barPeriod);
            this.mainTimeSeries = mainTimeSeries;
        }

        @Override
        protected T self() {
            return (T) this;
        }

        @Override
        public R build() {
            return (R) new BaseAggregatedTimeSeries(this);
        }
    }

}
