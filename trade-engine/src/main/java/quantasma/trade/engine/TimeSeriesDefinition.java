package quantasma.trade.engine;

import lombok.Data;
import quantasma.model.CandlePeriod;

@Data
public class TimeSeriesDefinition {

    private final CandlePeriod candlePeriod;
    private final int period;

    public TimeSeriesDefinition(CandlePeriod candlePeriod, int period) {
        this.candlePeriod = candlePeriod;
        this.period = period;
    }
}
