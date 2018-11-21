package quantasma.core.timeseries;

import org.ta4j.core.TimeSeries;

public interface ReferenceTimeSeries {

    TimeSeries source();

    TimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition);
}
