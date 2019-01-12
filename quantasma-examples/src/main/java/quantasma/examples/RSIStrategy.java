package quantasma.examples;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
import quantasma.core.order.CloseMarketOrder;
import quantasma.core.order.OpenMarketOrder;

import java.time.Instant;
import java.util.function.UnaryOperator;

@Slf4j
public class RSIStrategy extends BaseTradeStrategy {

    private final Position position = new Position();

    protected RSIStrategy(Builder builder) {
        super(builder);
    }

    @Override
    public boolean shouldEnter(int index, TradingRecord tradingRecord) {
        if (super.shouldEnter(index, tradingRecord) && !position.isOpened) {
            log.info("Opening position");
            getOrderService().execute(position.openOrder(1));
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldExit(int index, TradingRecord tradingRecord) {
        if (super.shouldExit(index, tradingRecord) && position.isOpened) {
            log.info("Closing position");
            getOrderService().execute(position.closeOrder());
            return true;
        }
        return false;
    }

    public static RSIStrategy buildBullish(Context context, Values<Parameter> parameterValues) {
        final Number rsiLowerBound = (Number) parameterValues.get(Parameter.RSI_LOWER_BOUND);
        final Number rsiUpperBound = (Number) parameterValues.get(Parameter.RSI_UPPER_BOUND);
        final RSIIndicator rsi = createRSIIndicator(context, parameterValues);
        return new Builder<>(context,
                             (String) parameterValues.get(Parameter.TRADE_SYMBOL),
                             new CrossedUpIndicatorRule(rsi, rsiLowerBound),
                             new CrossedDownIndicatorRule(rsi, rsiUpperBound),
                             parameterValues)
                .withName(String.format("bullish_rsi_strategy_%s-%s", rsiLowerBound, rsiUpperBound))
                .withUnstablePeriod((Integer) parameterValues.get(Parameter.RSI_PERIOD))
                .withAmount(1000)
                .build();
    }

    public static RSIStrategy buildBullish(Context context, UnaryOperator<Values<Parameter>> parameterValuesBuilder) {
        final Values<Parameter> parameterValues = parameterValuesBuilder.apply(Values.of(Parameter.class));
        return buildBullish(context, parameterValues);
    }

    private static RSIIndicator createRSIIndicator(Context context, Values<Parameter> parameterValues) {
        final TimeSeries timeSeries = context.getDataService().getMarketData()
                                             .of((String) parameterValues.get(Parameter.TRADE_SYMBOL))
                                             .getTimeSeries(BarPeriod.M1)
                                             .plainTimeSeries();
        final ClosePriceIndicator closePrice = new ClosePriceIndicator(timeSeries);
        return new RSIIndicator(closePrice, (Integer) parameterValues.get(Parameter.RSI_PERIOD));
    }

    @Override
    public Parameterizable[] parameterizables() {
        return Parameter.values();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private class Position {
        private String symbol = getTradeSymbol();
        private String label;
        private boolean isOpened;

        private OpenMarketOrder openOrder(double orderAmount) {
            setAmount(getNumFunction().apply(orderAmount));
            this.label = RSIStrategy.class.getSimpleName() + "_" + Instant.now().toEpochMilli() + "_" + symbol + "_" + String.valueOf(orderAmount).replace(".", "_");
            this.isOpened = true;
            return new OpenMarketOrder(label, orderAmount, symbol);
        }

        private CloseMarketOrder closeOrder() {
            if (!this.isOpened) {
                throw new IllegalStateException("Can't close unopened position");
            }

            this.isOpened = false;
            return new CloseMarketOrder(label);
        }
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
        RSI_LOWER_BOUND(Integer.class),
        RSI_UPPER_BOUND(Integer.class),
        RSI_PERIOD(Integer.class),
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
