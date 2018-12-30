package quantasma.core.timeseries;

import quantasma.core.timeseries.bar.OneSideBar;

public interface ManualIndexTimeSeries<B extends OneSideBar> extends UniversalTimeSeries<B> {
    void resetIndexes();

    void nextIndex();
}
