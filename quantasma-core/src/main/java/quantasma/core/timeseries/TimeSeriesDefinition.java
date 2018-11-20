package quantasma.core.timeseries;

import quantasma.core.CandlePeriod;

public interface TimeSeriesDefinition {

    CandlePeriod getCandlePeriod();

    int getPeriod();
}
