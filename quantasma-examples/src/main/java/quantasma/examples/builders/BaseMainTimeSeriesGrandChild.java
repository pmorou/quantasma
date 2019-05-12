package quantasma.examples.builders;

import quantasma.core.BarPeriod;
import quantasma.core.timeseries.BaseGenericTimeSeries;
import quantasma.core.timeseries.bar.OneSidedBar;

public class BaseMainTimeSeriesGrandChild<B extends OneSidedBar> extends BaseMainTimeSeriesChild<B> {
    protected BaseMainTimeSeriesGrandChild(Builder builder) {
        super(builder);
    }

    /**
     * @see BaseGenericTimeSeries.Builder
     */
    public static class Builder<T extends Builder<T, R>, R extends BaseMainTimeSeriesGrandChild> extends BaseMainTimeSeriesChild.Builder<T, R> {

        public Builder(String symbol, BarPeriod barPeriod) {
            super(symbol, barPeriod);
        }

        public T withGrandChild() {
            return self();
        }

        @Override
        protected T self() {
            return (T) this;
        }

        @Override
        public R build() {
            return (R) new BaseMainTimeSeriesGrandChild(this);
        }
    }

    public static void main(String[] args) {
        final BaseMainTimeSeriesGrandChild example = new Builder<>("example", BarPeriod.M1)
            .withName("from mother of all builders")
            .withChild() // Nearest extended builder
            .withGrandChild() // Current builder, type preserved
            .build();
    }
}
