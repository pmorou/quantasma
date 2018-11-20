package quantasma.core.analysis.criterion;

import org.ta4j.core.Order;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

public class FinishDepositCriterion extends AbstractAnalysisCriterion {

    private final double initialDeposit;
    private final ProfitLossPipsCalculator profitLossPipsCalculator;

    public FinishDepositCriterion(double initialDeposit, double pipResolution) {
        this.initialDeposit = initialDeposit;
        profitLossPipsCalculator = new ProfitLossPipsCalculator(pipResolution);
    }

    @Override
    public Num calculate(TimeSeries series, Trade trade) {
        return profitLossPipsCalculator.calculate(series, trade);
    }

    @Override
    public Num calculate(TimeSeries series, TradingRecord tradingRecord) {
        return tradingRecord.getTrades().stream()
                            .map(trade -> profitLossPipsCalculator.calculate(series, trade))
                            .reduce(series.numOf(initialDeposit), Num::plus);
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

    public static class ProfitLossPipsCalculator {

        final double pipResolution;

        public ProfitLossPipsCalculator(double pipResolution) {
            this.pipResolution = pipResolution;
        }

        public Num calculate(TimeSeries series, Trade trade) {
            if (trade.isClosed()) {
                Num exitClosePrice = hasPrice(trade.getExit()) ?
                        trade.getExit().getPrice() : series.getBar(trade.getExit().getIndex()).getClosePrice();
                Num entryClosePrice = hasPrice(trade.getEntry()) ?
                        trade.getEntry().getPrice() : series.getBar(trade.getEntry().getIndex()).getClosePrice();

                if (trade.getEntry().isBuy()) {
                    return exitClosePrice.minus(entryClosePrice).dividedBy(series.numOf(pipResolution));
                } else {
                    return entryClosePrice.minus(exitClosePrice).dividedBy(series.numOf(pipResolution));
                }
            }
            return series.numOf(0);
        }

        private boolean hasPrice(Order exit) {
            return !exit.getPrice().isNaN();
        }

    }
}
