package quantasma.core.timeseries;

import quantasma.core.timeseries.bar.OneSidedBar;

public interface ManualIndexTimeSeries<B extends OneSidedBar> extends GenericTimeSeries<B> {

    void resetIndexes();

    void nextIndex();

}
