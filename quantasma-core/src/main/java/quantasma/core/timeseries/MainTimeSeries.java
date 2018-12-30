package quantasma.core.timeseries;

import quantasma.core.timeseries.bar.OneSideBar;

public interface MainTimeSeries<B extends OneSideBar> extends UniversalTimeSeries<B> {

    AggregatedTimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition);
}
