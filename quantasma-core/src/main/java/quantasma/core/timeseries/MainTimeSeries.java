package quantasma.core.timeseries;

public interface MainTimeSeries extends UniversalTimeSeries {

    AggregatedTimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition);
}
