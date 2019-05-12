package quantasma.examples;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.Order;
import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import quantasma.core.BarPeriod;
import quantasma.core.BaseTradeStrategy;
import quantasma.core.Context;
import quantasma.core.analysis.parametrize.Parameterizable;
import quantasma.core.analysis.parametrize.Values;
import quantasma.core.order.CloseMarketOrder;
import quantasma.core.order.OpenMarketOrder;

import java.time.Instant;

@Slf4j
public abstract class RSIStrategy extends BaseTradeStrategy {

    private final Position position = new Position(selfClass());

    protected RSIStrategy(Builder builder) {
        super(builder);
    }

    @Override
    public boolean shouldEnter(int index, TradingRecord tradingRecord) {
        if (super.shouldEnter(index, tradingRecord) && !position.isOpened) {
            log.info("{} opening position", selfClass().getSimpleName());
            getOrderService().execute(position.openOrder(0.01, orderType()));
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldExit(int index, TradingRecord tradingRecord) {
        if (super.shouldExit(index, tradingRecord) && position.isOpened) {
            log.info("{} closing position", selfClass().getSimpleName());
            getOrderService().execute(position.closeOrder());
            return true;
        }
        return false;
    }

    protected static RSIIndicator createRSIIndicator(Context context, Values<Parameter> parameterValues) {
        var timeSeries = context.getDataService()
            .getMarketData()
            .of(parameterValues.getString(Parameter.TRADE_SYMBOL))
            .getTimeSeries(BarPeriod.M1)
            .plainTimeSeries();
        var closePriceIndicator = new ClosePriceIndicator(timeSeries);
        return new RSIIndicator(closePriceIndicator, parameterValues.getInteger(Parameter.RSI_PERIOD));
    }

    @SuppressWarnings("FinalStaticMethod")
    protected static final String createName(Class<?> clazz, Number rsiLowerBound, Number rsiUpperBound) {
        return String.format("%s_%s-%s", clazz.getSimpleName(), rsiLowerBound, rsiUpperBound);
    }

    @Override
    public Parameterizable[] parameterizables() {
        return Parameter.values();
    }

    protected abstract Class<?> selfClass();

    protected abstract Order.OrderType orderType();

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private class Position {
        private final Class<?> clazz;

        private String symbol = getTradeSymbol();
        private String label;
        private boolean isOpened;

        private OpenMarketOrder openOrder(double orderAmount, Order.OrderType orderType) {
            setAmount(getNumFunction().apply(orderAmount));
            this.label = clazz.getSimpleName()
                         + "_" + Instant.now().toEpochMilli()
                         + "_" + symbol
                         + "_" + String.valueOf(orderAmount).replace(".", "_");
            this.isOpened = true;
            return new OpenMarketOrder(label, orderAmount, symbol, orderType);
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
    protected abstract static class Builder<T extends Builder<T, R>, R extends RSIStrategy> extends BaseTradeStrategy.Builder<T, R> {

        protected Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule, Values<?> parameterValues) {
            super(context, tradeSymbol, entryRule, exitRule, parameterValues);
        }

        // New methods can be added here

        @Override
        protected abstract T self();

        @Override
        public abstract R build();
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
