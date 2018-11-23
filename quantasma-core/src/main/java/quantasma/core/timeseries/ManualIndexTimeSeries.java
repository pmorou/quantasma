package quantasma.core.timeseries;

import org.ta4j.core.TimeSeries;

public interface ManualIndexTimeSeries extends TimeSeries {
    void resetIndexes();

    void nextIndex();
}
