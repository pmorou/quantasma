package quantasma.core.analysis.criterion;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

public class FinishDepositCriterion extends AbstractAnalysisCriterion {

    private final double initialDeposit;
    private final ProfitLossCriterion profitLossPipsCalculator;

    public FinishDepositCriterion(double initialDeposit, double pipResolution) {
        this.initialDeposit = initialDeposit;
        profitLossPipsCalculator = new ProfitLossCriterion(pipResolution);
    }

    public Num calculate(BarSeries series, Trade trade) {
        return profitLossPipsCalculator.calculate(series, trade);
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        return null;
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return tradingRecord.getTrades()
            .stream()
            .map(trade -> profitLossPipsCalculator.calculate(series, trade))
            .reduce(series.numFactory().numOf(initialDeposit), Num::plus);
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

}
