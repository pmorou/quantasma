package quantasma.core.analysis.criterion;

import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

public class ProfitLossCriterion extends AbstractAnalysisCriterion {
    private static final int STANDARD_LOT_SIZE = 100_000;
    private static final int STANDARD_LOT_PROFIT = 10;

    private final ProfitLossPipsCriterion pipsCriterion;

    public ProfitLossCriterion(double pipResolution) {
        this.pipsCriterion = new ProfitLossPipsCriterion(pipResolution);
    }

    @Override
    public Num calculate(TimeSeries series, Trade trade) {
        return toRealUnit(series, trade, pipsCriterion.calculate(series, trade));
    }

    private static Num toRealUnit(TimeSeries timeSeries, Trade trade, Num pips) {
        return trade.getExit().getAmount()
                    .dividedBy(timeSeries.numOf(STANDARD_LOT_SIZE))
                    .multipliedBy(timeSeries.numOf(STANDARD_LOT_PROFIT))
                    .multipliedBy(pips);
    }

    @Override
    public Num calculate(TimeSeries series, TradingRecord tradingRecord) {
        return tradingRecord.getTrades()
                            .stream()
                            .map(trade -> calculate(series, trade))
                            .reduce(series.numOf(0), Num::plus);
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }
}