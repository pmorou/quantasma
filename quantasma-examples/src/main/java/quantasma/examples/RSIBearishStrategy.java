package quantasma.examples;

import org.ta4j.core.Rule;
import org.ta4j.core.Trade;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import quantasma.core.Context;
import quantasma.core.analysis.parametrize.Values;

import java.util.function.UnaryOperator;

public class RSIBearishStrategy extends RSIStrategy {

    protected RSIBearishStrategy(Builder builder) {
        super(builder);
    }

    @Override
    protected Class<?> selfClass() {
        return getClass();
    }

    @Override
    protected Trade.TradeType tradeType() {
        return Trade.TradeType.SELL;
    }

    public static RSIBearishStrategy build(Context context, Values<Parameter> parameterValues) {
        var rsiLowerBound = parameterValues.getInteger(Parameter.RSI_LOWER_BOUND);
        var rsiUpperBound = parameterValues.getInteger(Parameter.RSI_UPPER_BOUND);
        var rsiIndicator = createRSIIndicator(context, parameterValues);
        return new Builder<>(context,
            parameterValues.getString(Parameter.TRADE_SYMBOL),
            new CrossedDownIndicatorRule(rsiIndicator, rsiUpperBound),
            new CrossedUpIndicatorRule(rsiIndicator, rsiLowerBound),
            parameterValues)
            .withName(createName(RSIBearishStrategy.class, rsiLowerBound, rsiUpperBound))
            .withUnstableBars(parameterValues.getInteger(Parameter.RSI_PERIOD))
            .withAmount(1000)
            .build();
    }

    public static RSIBearishStrategy build(Context context, UnaryOperator<Values<Parameter>> parameterValuesBuilder) {
        var parameterValues = parameterValuesBuilder.apply(Values.of(Parameter.class));
        return build(context, parameterValues);
    }

    protected static class Builder<T extends RSIBearishStrategy.Builder<T, R>, R extends RSIBearishStrategy> extends RSIStrategy.Builder<T, R> {
        protected Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule, Values<?> parameterValues) {
            super(context, tradeSymbol, entryRule, exitRule, parameterValues);
        }

        @Override
        protected T self() {
            return (T) this;
        }

        @Override
        public R build() {
            return (R) new RSIBearishStrategy(this);
        }
    }
}
