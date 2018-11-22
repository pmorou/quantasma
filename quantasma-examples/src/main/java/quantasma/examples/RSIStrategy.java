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
import quantasma.core.BarPeriod;
import quantasma.core.Context;
import quantasma.core.TradeStrategy;
import quantasma.core.order.CloseMarkerOrder;
import quantasma.core.order.OpenMarketOrder;

import java.util.Objects;

@Slf4j
public class RSIStrategy extends TradeStrategy {

    private static final int MAX_NUMBER_OF_POSITIONS = 1;

    private int openedPositionsCounter = 0;

    public RSIStrategy(Context context, String name, Rule entryRule, Rule exitRule, int unstablePeriod) {
        super(Objects.requireNonNull(context), name, entryRule, exitRule, unstablePeriod);
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
        return openedPositionsCounter < MAX_NUMBER_OF_POSITIONS;
    }

    @Override
    public boolean shouldExit(int index, TradingRecord tradingRecord) {
        if (super.shouldExit(index, tradingRecord) && hasOpenedPosition()) {
            openedPositionsCounter--;
            System.out.println("Closing position");
            getOrderService().closePosition(new CloseMarkerOrder());
            return true;
        }
        return false;
    }

    private boolean hasOpenedPosition() {
        return openedPositionsCounter > 0;
    }

    public static Strategy buildBullish(Context context) {
        final RSIIndicator rsi = createRSIIndicator(context);
        return new RSIStrategy(context,
                               "RSI Strategy",
                               new CrossedUpIndicatorRule(rsi, 30),
                               new CrossedDownIndicatorRule(rsi, 70),
                               14);
    }

    public static Strategy buildBearish(Context context) {
        final RSIIndicator rsi = createRSIIndicator(context);
        return new RSIStrategy(context,
                               "RSI Strategy",
                               new CrossedDownIndicatorRule(rsi, 70),
                               new CrossedUpIndicatorRule(rsi, 30),
                               14);
    }

    private static void requireContext(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
    }

    private static RSIIndicator createRSIIndicator(Context context) {
        final TimeSeries timeSeries = context.getDataService().getMarketData().of("EURUSD").getTimeSeries(BarPeriod.M1);
        final ClosePriceIndicator closePrice = new ClosePriceIndicator(timeSeries);
        return new RSIIndicator(closePrice, 14);
    }

}
