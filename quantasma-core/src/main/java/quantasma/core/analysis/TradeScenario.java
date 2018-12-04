package quantasma.core.analysis;

import lombok.Data;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;
import quantasma.core.analysis.parametrize.Parameters;

@Data
public class TradeScenario {

    private final TimeSeries timeSeries;
    private final Parameters parameters;
    private final TradingRecord tradingRecord;

}
