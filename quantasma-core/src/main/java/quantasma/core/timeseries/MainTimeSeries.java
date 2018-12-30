package quantasma.core.timeseries;

import quantasma.core.timeseries.bar.OneSidedBar;

public interface MainTimeSeries<B extends OneSidedBar> extends UniversalTimeSeries<B> {

    AggregatedTimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition);
}
