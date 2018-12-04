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
                final Num exitClosePrice = getPrice(series, trade.getExit());
                final Num entryClosePrice = getPrice(series, trade.getEntry());
                final Num pips = difference(trade, exitClosePrice, entryClosePrice)
                        .dividedBy(series.numOf(pipResolution));
                return toRealUnit(series, trade, pips);
            }
            return series.numOf(0);
        }

        private Num getPrice(TimeSeries series, Order exit) {
            return hasPrice(exit) ?
                    exit.getPrice() : series.getBar(exit.getIndex()).getClosePrice();
        }

        private Num difference(Trade trade, Num exitClosePrice, Num entryClosePrice) {
            return trade.getEntry().isBuy() ?
                    exitClosePrice.minus(entryClosePrice) : entryClosePrice.minus(exitClosePrice);
        }

        private boolean hasPrice(Order exit) {
            return !exit.getPrice().isNaN();
        }

        private final static int STANDARD_LOT_SIZE = 100_000;
        private final static int STANDARD_LOT_PROFIT = 10;

        private static Num toRealUnit(TimeSeries timeSeries, Trade trade, Num pips) {
            return trade.getExit().getAmount()
                        .dividedBy(timeSeries.numOf(STANDARD_LOT_SIZE))
                        .multipliedBy(timeSeries.numOf(STANDARD_LOT_PROFIT))
                        .multipliedBy(pips);
        }
    }
}
