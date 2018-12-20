package quantasma.core.analysis.criterion;

import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

public class AvgProfitLossCriterion extends AbstractAnalysisCriterion {
    private final ProfitLossCriterion profitLossCriterion;

    public AvgProfitLossCriterion(double pipResolution) {
        this.profitLossCriterion = new ProfitLossCriterion(pipResolution);
    }

    @Override
    public Num calculate(TimeSeries series, Trade trade) {
        return profitLossCriterion.calculate(series, trade);
    }

    @Override
    public Num calculate(TimeSeries series, TradingRecord tradingRecord) {
        return profitLossCriterion.calculate(series, tradingRecord)
                                  .dividedBy(series.numOf(tradingRecord.getTradeCount()));
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }
}