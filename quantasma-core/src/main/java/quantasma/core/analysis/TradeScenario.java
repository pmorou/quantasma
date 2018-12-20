package quantasma.core.analysis;

import lombok.Data;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;
import quantasma.core.analysis.parametrize.Values;

@Data
public class TradeScenario {

    private final TimeSeries timeSeries;
    private final Values values;
    private final TradingRecord tradingRecord;

}
