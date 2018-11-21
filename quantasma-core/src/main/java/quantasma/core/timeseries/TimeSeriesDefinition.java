package quantasma.core.timeseries;

import quantasma.core.BarPeriod;

public interface TimeSeriesDefinition {

    BarPeriod getBarPeriod();

    int getPeriod();
}
