package quantasma.examples;

import org.ta4j.core.Order;
import org.ta4j.core.Rule;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import quantasma.core.Context;
import quantasma.core.analysis.parametrize.Values;

import java.util.function.UnaryOperator;

public class RSIBullishStrategy extends RSIStrategy {

    protected RSIBullishStrategy(Builder builder) {
        super(builder);
    }

    @Override
    protected Class<?> selfClass() {
        return getClass();
    }

    @Override
    protected Order.OrderType orderType() {
        return Order.OrderType.BUY;
    }

    public static RSIBullishStrategy build(Context context, Values<Parameter> parameterValues) {
        var rsiLowerBound = parameterValues.getInteger(Parameter.RSI_LOWER_BOUND);
        var rsiUpperBound = parameterValues.getInteger(Parameter.RSI_UPPER_BOUND);
        var rsiIndicator = createRSIIndicator(context, parameterValues);
        return new Builder<>(context,
            parameterValues.getString(Parameter.TRADE_SYMBOL),
            new CrossedUpIndicatorRule(rsiIndicator, rsiLowerBound),
            new CrossedDownIndicatorRule(rsiIndicator, rsiUpperBound),
            parameterValues)
            .withName(createName(RSIBullishStrategy.class, rsiLowerBound, rsiUpperBound))
            .withUnstablePeriod(parameterValues.getInteger(Parameter.RSI_PERIOD))
            .withAmount(1000)
            .build();
    }

    public static RSIBullishStrategy build(Context context, UnaryOperator<Values<Parameter>> parameterValuesBuilder) {
        var parameterValues = parameterValuesBuilder.apply(Values.of(Parameter.class));
        return build(context, parameterValues);
    }

    protected static class Builder<T extends RSIBullishStrategy.Builder<T, R>, R extends RSIBullishStrategy> extends RSIStrategy.Builder<T, R> {
        protected Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule, Values<?> parameterValues) {
            super(context, tradeSymbol, entryRule, exitRule, parameterValues);
        }

        @Override
        protected T self() {
            return (T) this;
        }

        @Override
        public R build() {
            return (R) new RSIBullishStrategy(this);
        }
    }
}
