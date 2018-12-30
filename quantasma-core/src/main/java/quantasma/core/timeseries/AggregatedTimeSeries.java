package quantasma.core.timeseries;

import quantasma.core.timeseries.bar.OneSidedBar;

public interface AggregatedTimeSeries<B extends OneSidedBar> extends UniversalTimeSeries<B> {

    MainTimeSeries getMainTimeSeries();

}
