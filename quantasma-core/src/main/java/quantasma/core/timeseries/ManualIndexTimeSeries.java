package quantasma.core.timeseries;

import org.ta4j.core.Bar;

public interface ManualIndexTimeSeries<B extends Bar> extends UniversalTimeSeries<B> {
    void resetIndexes();

    void nextIndex();
}
