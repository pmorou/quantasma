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
import quantasma.core.analysis.parametrize.Parameter;
import quantasma.core.analysis.parametrize.Parameters;
import quantasma.core.order.CloseMarkerOrder;
import quantasma.core.order.OpenMarketOrder;

@Slf4j
public final class RSIStrategy extends BaseTradeStrategy {

    private static final int MAX_NUMBER_OF_POSITIONS = 1;

    private int openedPositionsCounter = 0;

    private RSIStrategy(Builder builder) {
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

    public static RSIStrategy buildBullish(Context context, Parameters parameters)  {
        final Number rsiLowerBound = (Number) parameters.get(ParameterList.RSI_LOWER_BOUND);
        final Number rsiUpperBound = (Number) parameters.get(ParameterList.RSI_UPPER_BOUND);
        final RSIIndicator rsi = createRSIIndicator(context, parameters);
        return new Builder<>(context,
                             (String) parameters.get(ParameterList.TRADE_SYMBOL),
                             new CrossedUpIndicatorRule(rsi, rsiLowerBound),
                             new CrossedDownIndicatorRule(rsi, rsiUpperBound),
                             parameters)
                .withName(String.format("bullish_rsi_strategy_%s-%s", rsiLowerBound, rsiUpperBound))
                .withUnstablePeriod((Integer) parameters.get(ParameterList.RSI_PERIOD))
                .withAmount(1000)
                .build();
    }

    private static RSIIndicator createRSIIndicator(Context context, Parameters parameters) {
        final TimeSeries timeSeries = context.getDataService().getMarketData()
                                             .of((String) parameters.get(ParameterList.TRADE_SYMBOL))
                                             .getTimeSeries(BarPeriod.M1);
        final ClosePriceIndicator closePrice = new ClosePriceIndicator(timeSeries);
        return new RSIIndicator(closePrice, (Integer) parameters.get(ParameterList.RSI_PERIOD));
    }

    /**
     * @see quantasma.core.BaseTradeStrategy.Builder
     */
    private static class Builder<T extends Builder<T, R>, R extends RSIStrategy> extends BaseTradeStrategy.Builder<T, R> {

        private Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule, Parameters parameters) {
            super(context, tradeSymbol, entryRule, exitRule, parameters);
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

    /**
     * Allowed parametrization settings
     */
    public enum ParameterList implements Parameter {
        RSI_LOWER_BOUND(Number.class),
        RSI_UPPER_BOUND(Number.class),
        RSI_PERIOD(Number.class),
        TRADE_SYMBOL(String.class);

        private final Class<?> clazz;

        ParameterList(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Class<?> clazz() {
            return clazz;
        }
    }

}
