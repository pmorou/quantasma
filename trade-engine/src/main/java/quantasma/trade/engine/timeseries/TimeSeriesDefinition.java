package quantasma.trade.engine.timeseries;

import quantasma.model.CandlePeriod;

public interface TimeSeriesDefinition {

    CandlePeriod getCandlePeriod();

    int getPeriod();
}
