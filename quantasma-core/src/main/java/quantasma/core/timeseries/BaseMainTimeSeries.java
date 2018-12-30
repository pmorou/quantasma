package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import quantasma.core.BarPeriod;

public class BaseMainTimeSeries<B extends Bar> extends BaseUniversalTimeSeries<B> implements MainTimeSeries<B> {

    protected BaseMainTimeSeries(Builder builder) {
        super(builder);
    }

    public static MainTimeSeries create(TimeSeriesDefinition timeSeriesDefinition, String symbol) {
        return new BaseMainTimeSeries.Builder<>(symbol, timeSeriesDefinition.getBarPeriod())
                .withName(timeSeriesDefinition.getBarPeriod().getPeriodCode())
                .withMaxBarCount(timeSeriesDefinition.getMaxBarCount())
                .build();
    }

    @Override
    public AggregatedTimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition) {
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

        public R build() {
            return (R) new BaseMainTimeSeries(this);
        }
    }
}