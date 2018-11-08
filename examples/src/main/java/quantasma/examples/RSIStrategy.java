package quantasma.examples;

import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import quantasma.model.CandlePeriod;
import quantasma.trade.engine.Context;
import quantasma.trade.engine.TradeStrategy;
import quantasma.trade.engine.order.OpenMarketOrder;

@Slf4j
public class RSIStrategy extends TradeStrategy {

    private final int maxNumberOfPositions = 1;

    private int openedPositionsCounter = 0;

    public RSIStrategy(Context context, String name, Rule entryRule, Rule exitRule, int unstablePeriod) {
        super(context, name, entryRule, exitRule, unstablePeriod);
    }

    @Override
    public boolean shouldEnter(int index, TradingRecord tradingRecord) {
        if (super.shouldEnter(index, tradingRecord) && canOpenPosition()) {
            openedPositionsCounter++;
            log.info("Opening position");
            getOrderService().openPosition(new OpenMarketOrder(1, "EURUSD"));
            return true;
        }
        return false;
    }

    private boolean canOpenPosition() {
        return openedPositionsCounter < maxNumberOfPositions;
    }

    @Override
    public boolean shouldExit(int index, TradingRecord tradingRecord) {
        if (super.shouldExit(index, tradingRecord) && hasOpenedPosition()) {
            openedPositionsCounter--;
            System.out.println("Closing position");
            getOrderService().closePosition(null);
            return true;
        }
        return false;
    }

    private boolean hasOpenedPosition() {
        return openedPositionsCounter > 0;
    }

    public static Strategy build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        final TimeSeries timeSeries = context.getDataService().getMultipleTimeSeries("EURUSD").getTimeSeries(CandlePeriod.M1);
        final ClosePriceIndicator closePrice = new ClosePriceIndicator(timeSeries);
        final RSIIndicator rsi = new RSIIndicator(closePrice, 14);

        return new RSIStrategy(context,
                               "RSI Strategy",
                               new CrossedUpIndicatorRule(rsi, 30),
                               new CrossedDownIndicatorRule(rsi, 70),
                               14);
    }

}
