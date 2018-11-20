package quantasma.core.timeseries;

import quantasma.model.CandlePeriod;

public interface TimeSeriesDefinition {

    CandlePeriod getCandlePeriod();

    int getPeriod();
}
