package quantasma.core.analysis.criterion;

import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

public class TradesCountCriterion extends AbstractAnalysisCriterion {
    @Override
    public Num calculate(TimeSeries series, Trade trade) {
        return series.numOf(1);
    }

    @Override
    public Num calculate(TimeSeries series, TradingRecord tradingRecord) {
        return series.numOf(tradingRecord.getTradeCount());
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isLessThan(criterionValue2);
    }
}
