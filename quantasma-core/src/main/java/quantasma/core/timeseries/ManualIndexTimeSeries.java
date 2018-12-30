package quantasma.core.timeseries;

import quantasma.core.timeseries.bar.OneSidedBar;

public interface ManualIndexTimeSeries<B extends OneSidedBar> extends UniversalTimeSeries<B> {
    void resetIndexes();

    void nextIndex();
}
