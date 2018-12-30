package quantasma.core.timeseries;

import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.OneSideBar;
import quantasma.core.timeseries.bar.factory.BarFactory;

public class BaseMainTimeSeries<B extends OneSideBar> extends BaseUniversalTimeSeries<B> implements MainTimeSeries<B> {

    protected BaseMainTimeSeries(Builder builder) {
        super(builder);
    }

    public static <B extends OneSideBar> MainTimeSeries<B> create(TimeSeriesDefinition timeSeriesDefinition, String symbol, BarFactory<B> barFactory) {
        return new BaseMainTimeSeries.Builder<>(symbol, timeSeriesDefinition.getBarPeriod(), barFactory)
                .withName(timeSeriesDefinition.getBarPeriod().getPeriodCode())
                .withMaxBarCount(timeSeriesDefinition.getMaxBarCount())
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

        public Builder(String symbol, BarPeriod barPeriod, BarFactory<?> barFactory) {
            super(symbol, barPeriod, barFactory);
        }

        @Override
        protected T self() {
            return (T) this;
        }

        public R build() {
            return (R) new BaseMainTimeSeries(this);
        }
    }
}