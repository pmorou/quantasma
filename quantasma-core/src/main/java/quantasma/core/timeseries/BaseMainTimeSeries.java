package quantasma.core.timeseries;

import quantasma.core.BarPeriod;

public class BaseMainTimeSeries extends BaseSymbolTimeSeries implements MainTimeSeries {

    protected BaseMainTimeSeries(String name, String symbol, BarPeriod barPeriod) {
        super(name, symbol, barPeriod);
    }

    protected BaseMainTimeSeries(String name, String symbol, BarPeriod barPeriod, int maxBarCount) {
        super(name, symbol, barPeriod, maxBarCount);
    }

    protected BaseMainTimeSeries(Builder builder) {
        super(builder);
    }

    public static MainTimeSeries create(TimeSeriesDefinition timeSeriesDefinition, String symbol) {
        return new BaseMainTimeSeries(timeSeriesDefinition.getBarPeriod().getPeriodCode(),
                                      symbol,
                                      timeSeriesDefinition.getBarPeriod(),
                                      timeSeriesDefinition.getMaxBarCount());
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
    public static class Builder<T extends Builder<T, R>, R extends BaseMainTimeSeries> extends BaseSymbolTimeSeries.Builder<T, R> {

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