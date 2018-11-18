package quantasma.trade.engine.timeseries;

import lombok.Data;
import quantasma.model.CandlePeriod;

@Data
public class TimeSeriesDefinitionImpl implements TimeSeriesDefinition {

    private final CandlePeriod candlePeriod;
    private final int period;

    public TimeSeriesDefinitionImpl(CandlePeriod candlePeriod, int period) {
        this.candlePeriod = candlePeriod;
        this.period = period;
    }

}
