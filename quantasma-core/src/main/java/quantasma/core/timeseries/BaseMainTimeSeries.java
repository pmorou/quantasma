package quantasma.core.timeseries;

import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.BarFactory;
import quantasma.core.timeseries.bar.OneSidedBar;

public class BaseMainTimeSeries<B extends OneSidedBar> extends BaseUniversalTimeSeries<B> implements MainTimeSeries<B> {

    protected BaseMainTimeSeries(Builder builder) {
        super(builder);
    }

    public static <B extends OneSidedBar> MainTimeSeries<B> create(TimeSeriesDefinition timeSeriesDefinition, String symbol) {
        return new BaseMainTimeSeries.Builder<>(symbol, timeSeriesDefinition.getBarPeriod())
                .withName(timeSeriesDefinition.getBarPeriod().getPeriodCode())
                .withMaxBarCount(timeSeriesDefinition.getMaxBarCount())
                .build();
    }

    public static <B extends OneSidedBar> MainTimeSeries<B> create(TimeSeriesDefinition timeSeriesDefinition, String symbol, BarFactory<B> barFactory) {
        return new BaseMainTimeSeries.Builder<>(symbol, timeSeriesDefinition.getBarPeriod())
                .withName(timeSeriesDefinition.getBarPeriod().getPeriodCode())
                .withMaxBarCount(timeSeriesDefinition.getMaxBarCount())
                .withBarFactory(barFactory)
                .build();
    }

    @Override
    public AggregatedTimeSeries<B> aggregate(TimeSeriesDefinition timeSeriesDefinition) {
        return AggregatedTimeSeriesFactory.from(this).createInstance(timeSeriesDefinition);
    }

    /**
     * Example of builder which preserves all methods of its parents<p>
     *
     * @param <T> Builder type
     * @param <R> {@code build()} return type
     */
    public static class Builder<T extends Builder<T, R>, R extends BaseMainTimeSeries> extends BaseUniversalTimeSeries.Builder<T, R> {

        public Builder(String symbol, BarPeriod barPeriod) {
            super(symbol, barPeriod);
        }

        @Override
        protected T self() {
            return (T) this;
        }

        @Override
        public R build() {
            return (R) new BaseMainTimeSeries(this);
        }
    }
}