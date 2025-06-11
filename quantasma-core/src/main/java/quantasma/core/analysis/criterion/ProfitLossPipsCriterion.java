package quantasma.core.analysis.criterion;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

public class ProfitLossPipsCriterion extends AbstractAnalysisCriterion {

    private final double pipResolution;

    public ProfitLossPipsCriterion(double pipResolution) {
        this.pipResolution = pipResolution;
    }

    private static Num difference(Position position, Num exitClosePrice, Num entryClosePrice) {
        return position.getStartingType() == Trade.TradeType.BUY ?
            exitClosePrice.minus(entryClosePrice) : entryClosePrice.minus(exitClosePrice);
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        if (position.isClosed()) {
            final Num exitClosePrice = position.getExit().getNetPrice();
            final Num entryClosePrice = position.getEntry().getNetPrice();
            return difference(position, exitClosePrice, entryClosePrice)
              .dividedBy(series.numFactory().numOf(pipResolution));
        }
        return series.numFactory().zero();
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return tradingRecord.getTrades()
            .stream()
            .map(Trade::getNetPrice)
            .reduce(series.numFactory().zero(), Num::plus);
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }
}
