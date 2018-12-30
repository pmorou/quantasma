package quantasma.examples.builders;

import quantasma.core.BarPeriod;
import quantasma.core.timeseries.BaseMainTimeSeries;
import quantasma.core.timeseries.BaseUniversalTimeSeries;
import quantasma.core.timeseries.bar.OneSideBar;
import quantasma.core.timeseries.bar.factory.BarFactory;
import quantasma.core.timeseries.bar.factory.BidAskBarFactory;

public class BaseMainTimeSeriesChild<B extends OneSideBar> extends BaseMainTimeSeries<B> {
    protected BaseMainTimeSeriesChild(Builder builder) {
        super(builder);
    }

    /**
     * @see BaseUniversalTimeSeries.Builder
     */
    public static class Builder<T extends Builder<T, R>, R extends BaseMainTimeSeriesChild> extends BaseMainTimeSeries.Builder<T, R> {

        public Builder(String symbol, BarPeriod barPeriod, BarFactory<?> barFactory) {
            super(symbol, barPeriod, barFactory);
        }

        public T withChild() {
            return self();
        }

        @Override
        protected T self() {
            return (T) this;
        }

        @Override
        public R build() {
            return (R) new BaseMainTimeSeriesChild(this);
        }
    }

    public static void main(String[] args) {
        final BaseMainTimeSeriesChild example = new Builder<>("example", BarPeriod.M1, new BidAskBarFactory())
                .withName("from mother of all builders")
                .withChild() // Current builder, type preserved
                .build();
    }
}
