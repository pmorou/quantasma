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
import quantasma.core.analysis.parametrize.Parameterizable;
import quantasma.core.analysis.parametrize.Values;
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

    public static RSIStrategy buildBullish(Context context, Values<Parameter> values)  {
        final Number rsiLowerBound = (Number) values.get(Parameter.RSI_LOWER_BOUND);
        final Number rsiUpperBound = (Number) values.get(Parameter.RSI_UPPER_BOUND);
        final RSIIndicator rsi = createRSIIndicator(context, values);
        return new Builder<>(context,
                             (String) values.get(Parameter.TRADE_SYMBOL),
                             new CrossedUpIndicatorRule(rsi, rsiLowerBound),
                             new CrossedDownIndicatorRule(rsi, rsiUpperBound),
                             values)
                .withName(String.format("bullish_rsi_strategy_%s-%s", rsiLowerBound, rsiUpperBound))
                .withUnstablePeriod((Integer) values.get(Parameter.RSI_PERIOD))
                .withAmount(1000)
                .build();
    }

    private static RSIIndicator createRSIIndicator(Context context, Values<Parameter> values) {
        final TimeSeries timeSeries = context.getDataService().getMarketData()
                                             .of((String) values.get(Parameter.TRADE_SYMBOL))
                                             .getTimeSeries(BarPeriod.M1);
        final ClosePriceIndicator closePrice = new ClosePriceIndicator(timeSeries);
        return new RSIIndicator(closePrice, (Integer) values.get(Parameter.RSI_PERIOD));
    }

    /**
     * @see quantasma.core.BaseTradeStrategy.Builder
     */
    protected static class Builder<T extends Builder<T, R>, R extends RSIStrategy> extends BaseTradeStrategy.Builder<T, R> {

        protected Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule, Values<?> parameterValues) {
            super(context, tradeSymbol, entryRule, exitRule, parameterValues);
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
    public enum Parameter implements Parameterizable {
        RSI_LOWER_BOUND(Number.class),
        RSI_UPPER_BOUND(Number.class),
        RSI_PERIOD(Number.class),
        TRADE_SYMBOL(String.class);

        private final Class<?> clazz;

        Parameter(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Class<?> clazz() {
            return clazz;
        }
    }

}
