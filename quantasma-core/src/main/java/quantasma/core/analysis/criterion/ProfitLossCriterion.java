package quantasma.core.analysis.criterion;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

public class ProfitLossCriterion extends AbstractAnalysisCriterion {
    private static final int STANDARD_LOT_SIZE = 100_000;
    private static final int STANDARD_LOT_PROFIT = 10;

    private final ProfitLossPipsCriterion pipsCriterion;

    public ProfitLossCriterion(double pipResolution) {
        this.pipsCriterion = new ProfitLossPipsCriterion(pipResolution);
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        return this.pipsCriterion.calculate(series, position);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.pipsCriterion.calculate(series, tradingRecord);
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

    public Num calculate(BarSeries series, Trade trade) {
        return trade.getNetPrice();
    }
}