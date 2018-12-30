package quantasma.core.timeseries;

import lombok.AccessLevel;
import lombok.Getter;
import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.BidAskBar;
import quantasma.core.timeseries.bar.OneSidedBar;
import quantasma.core.timeseries.bar.factory.BarFactory;

public class BaseAggregatedTimeSeries<B extends OneSidedBar> extends BaseUniversalTimeSeries<B> implements AggregatedTimeSeries<B> {
    @Getter
    private final MainTimeSeries mainTimeSeries;

    protected BaseAggregatedTimeSeries(Builder builder) {
        super(builder);
        this.mainTimeSeries = builder.mainTimeSeries;
    }

    @Override
    public void addBar(B bar, boolean replace) {
        super.addBar(bar, replace);
    }

    @Override
    public B getFirstBar() {
        // avoid index manipulation
        return super.getBar(getBeginIndex());
    }

    @Override
    public B getLastBar() {
        // avoid index manipulation
        return super.getBar(getEndIndex());
    }

    @Override
    public B getBar(int index) {
        if (index == mainTimeSeries.getEndIndex()) {
            return getLastBar();
        }

        final int nthOldElement = mainTimeSeries.getEndIndex() - index;

        if (nthOldElement < getBarCount()) {
            return super.getBar(getEndIndex() - nthOldElement);
        }

        return (B) BidAskBar.NaN; // TODO: provide generic method
    }

    public static class Builder<T extends Builder<T, R>, R extends BaseAggregatedTimeSeries> extends BaseUniversalTimeSeries.Builder<T, R> {
        @Getter(value = AccessLevel.PROTECTED)
        private final MainTimeSeries mainTimeSeries;

        public Builder(String symbol, BarPeriod barPeriod, MainTimeSeries mainTimeSeries, BarFactory<?> barFactory) {
            super(symbol, barPeriod, barFactory);
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
