package quantasma.core.timeseries;

import quantasma.core.BarPeriod;

public class BaseMainTimeSeries extends BaseSymbolTimeSeries implements MainTimeSeries {

    protected BaseMainTimeSeries(String name, String symbol, BarPeriod barPeriod) {
        super(name, symbol, barPeriod);
    }

    protected BaseMainTimeSeries(String name, String symbol, BarPeriod barPeriod, int maxBarCount) {
        super(name, symbol, barPeriod, maxBarCount);
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

}