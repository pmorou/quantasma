package quantasma.core.analysis.criterion;

import org.ta4j.core.Order;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

public class ProfitLossPipsCriterion extends AbstractAnalysisCriterion {

    private final double pipResolution;

    public ProfitLossPipsCriterion(double pipResolution) {
        this.pipResolution = pipResolution;
    }

    @Override
    public Num calculate(TimeSeries series, Trade trade) {
        if (trade.isClosed()) {
            final Num exitClosePrice = getPrice(series, trade.getExit());
            final Num entryClosePrice = getPrice(series, trade.getEntry());
            return difference(trade, exitClosePrice, entryClosePrice)
                .dividedBy(series.numOf(pipResolution));
        }
        return series.numOf(0);
    }

    private static Num getPrice(TimeSeries series, Order exit) {
        return hasPrice(exit) ?
            exit.getPrice() : series.getBar(exit.getIndex()).getClosePrice();
    }

    private static boolean hasPrice(Order exit) {
        return !exit.getPrice().isNaN();
    }

    private static Num difference(Trade trade, Num exitClosePrice, Num entryClosePrice) {
        return trade.getEntry().isBuy() ?
            exitClosePrice.minus(entryClosePrice) : entryClosePrice.minus(exitClosePrice);
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
