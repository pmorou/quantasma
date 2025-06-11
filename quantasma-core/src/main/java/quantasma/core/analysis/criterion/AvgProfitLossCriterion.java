package quantasma.core.analysis.criterion;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

public class AvgProfitLossCriterion extends AbstractAnalysisCriterion {
    private final ProfitLossCriterion profitLossCriterion;

    public AvgProfitLossCriterion(double pipResolution) {
        this.profitLossCriterion = new ProfitLossCriterion(pipResolution);
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        return position.isClosed() ? series.numFactory().one() : series.numFactory().zero();
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return profitLossCriterion.calculate(series, tradingRecord)
            .dividedBy(series.numFactory().numOf(tradingRecord.getTrades().size()));
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }
}