package quantasma.core.timeseries;

import org.ta4j.core.TimeSeries;

public interface AggregableTimeSeries {

    TimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition);
}
