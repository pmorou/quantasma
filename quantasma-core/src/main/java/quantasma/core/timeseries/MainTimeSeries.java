package quantasma.core.timeseries;

public interface MainTimeSeries extends SymbolTimeSeries {

    AggregatedTimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition);
}
