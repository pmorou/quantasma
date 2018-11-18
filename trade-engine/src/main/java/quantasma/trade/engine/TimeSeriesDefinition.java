package quantasma.trade.engine;

import quantasma.model.CandlePeriod;

public interface TimeSeriesDefinition {

    CandlePeriod getCandlePeriod();

    int getPeriod();
}
