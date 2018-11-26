package quantasma.examples;

import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import quantasma.core.BarPeriod;
import quantasma.core.BaseTradeStrategy;
import quantasma.core.Context;
import quantasma.core.order.CloseMarkerOrder;
import quantasma.core.order.OpenMarketOrder;

@Slf4j
public class RSIStrategy extends BaseTradeStrategy {

    private static final int MAX_NUMBER_OF_POSITIONS = 1;

    private int openedPositionsCounter = 0;

    protected RSIStrategy(Builder builder) {
        super(builder);
    }

    @Override
    public boolean shouldEnter(int index, TradingRecord tradingRecord) {
        if (super.shouldEnter(index, tradingRecord) && shouldOpenPosition()) {
            openedPositionsCounter++;
            log.info("Opening position");
            getOrderService().openPosition(new OpenMarketOrder(1, getTradeSymbol()));
            return true;
        }
        return false;
    }

    private boolean shouldOpenPosition() {
        return openedPositionsCounter < MAX_NUMBER_OF_POSITIONS;
    }

    @Override
    public boolean shouldExit(int index, TradingRecord tradingRecord) {
        if (super.shouldExit(index, tradingRecord) && hasOpenedPosition()) {
            openedPositionsCounter--;
            log.info("Closing position");
            getOrderService().closePosition(new CloseMarkerOrder());
            return true;
        }
        return false;
    }

    private boolean hasOpenedPosition() {
        return openedPositionsCounter > 0;
    }

    public static RSIStrategy buildBullish(Context context, String tradeSymbol, BarPeriod barPeriod) {
        final RSIIndicator rsi = createRSIIndicator(context, tradeSymbol, barPeriod);
        return new Builder<>(context, tradeSymbol, new CrossedUpIndicatorRule(rsi, 30), new CrossedDownIndicatorRule(rsi, 70))
                .withName("bullish_rsi_strategy_30-70")
                .withUnstablePeriod(14)
                .build();
    }

    public static RSIStrategy buildBearish(Context context, String tradeSymbol, BarPeriod barPeriod) {
        final RSIIndicator rsi = createRSIIndicator(context, tradeSymbol, barPeriod);
        return new Builder<>(context, tradeSymbol, new CrossedDownIndicatorRule(rsi, 70), new CrossedUpIndicatorRule(rsi, 30))
                .withName("bearish_rsi_strategy_30-70")
                .withUnstablePeriod(14)
                .build();
    }

    private static RSIIndicator createRSIIndicator(Context context, String tradeSymbol, BarPeriod barPeriod) {
        final TimeSeries timeSeries = context.getDataService().getMarketData().of(tradeSymbol).getTimeSeries(barPeriod);
        final ClosePriceIndicator closePrice = new ClosePriceIndicator(timeSeries);
        return new RSIIndicator(closePrice, 14);
    }

    /**
     * @see quantasma.core.BaseTradeStrategy.Builder
     */
    public static class Builder<T extends Builder<T, R>, R extends RSIStrategy> extends BaseTradeStrategy.Builder<T, R> {

        public Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule) {
            super(context, tradeSymbol, entryRule, exitRule);
        }

        // New methods can be added here

        @Override
        protected T self() {
            return (T) this;
        }

        @Override
        public R build() {
            return (R) new RSIStrategy(this);
        }
    }
}
