package quantasma.examples.builders;

import quantasma.core.BarPeriod;
import quantasma.core.timeseries.BaseMainTimeSeries;
import quantasma.core.timeseries.BaseGenericTimeSeries;
import quantasma.core.timeseries.bar.OneSidedBar;

public class BaseMainTimeSeriesChild<B extends OneSidedBar> extends BaseMainTimeSeries<B> {
    protected BaseMainTimeSeriesChild(Builder builder) {
        super(builder);
    }

    /**
     * @see BaseGenericTimeSeries.Builder
     */
    public static class Builder<T extends Builder<T, R>, R extends BaseMainTimeSeriesChild> extends BaseMainTimeSeries.Builder<T, R> {

        public Builder(String symbol, BarPeriod barPeriod) {
            super(symbol, barPeriod);
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
        final BaseMainTimeSeriesChild example = new Builder<>("example", BarPeriod.M1)
            .withName("from mother of all builders")
            .withChild() // Current builder, type preserved
            .build();
    }
}
