package quantasma.core.timeseries;

import quantasma.core.timeseries.bar.OneSideBar;

public interface AggregatedTimeSeries<B extends OneSideBar> extends UniversalTimeSeries<B> {

    MainTimeSeries getMainTimeSeries();

}
