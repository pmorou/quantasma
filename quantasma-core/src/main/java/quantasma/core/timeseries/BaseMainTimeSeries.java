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
        return AggregatedTimeSeriesFactory.from(this, getSymbol()).createInstance(timeSeriesDefinition);
    }

    public static final class Builder<T> extends BaseSymbolTimeSeries.Builder {

        public Builder(String symbol, BarPeriod barPeriod) {
            super(symbol, barPeriod);
        }

        @Override
        protected Builder<T> self() {
            return this;
        }

        public BaseMainTimeSeries build() { return new BaseMainTimeSeries(this); }
    }
}