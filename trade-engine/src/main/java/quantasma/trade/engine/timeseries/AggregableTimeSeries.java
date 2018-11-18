package quantasma.trade.engine.timeseries;

import org.ta4j.core.TimeSeries;

public interface AggregableTimeSeries {

    TimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition);
}
