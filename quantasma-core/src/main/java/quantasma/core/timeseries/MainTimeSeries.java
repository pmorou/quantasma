package quantasma.core.timeseries;

import org.ta4j.core.Bar;

public interface MainTimeSeries<B extends Bar> extends UniversalTimeSeries<B> {

    AggregatedTimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition);
}
