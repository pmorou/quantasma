package quantasma.core.timeseries;

import org.ta4j.core.TimeSeries;

public interface MainTimeSeries extends TimeSeries {

    AggregatedTimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition);
}
