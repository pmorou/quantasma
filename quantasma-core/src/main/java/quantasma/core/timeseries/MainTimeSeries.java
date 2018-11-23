package quantasma.core.timeseries;

import org.ta4j.core.TimeSeries;

public interface MainTimeSeries extends TimeSeries {

    TimeSeries source();

    AggregatedTimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition);
}
