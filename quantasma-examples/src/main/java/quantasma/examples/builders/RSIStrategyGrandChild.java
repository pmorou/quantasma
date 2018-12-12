package quantasma.examples.builders;

import org.ta4j.core.Rule;
import quantasma.core.BaseContext;
import quantasma.core.Context;
import quantasma.core.analysis.parametrize.Values;

public class RSIStrategyGrandChild extends RSIStrategyChild {
    protected RSIStrategyGrandChild(Builder builder) {
        super(builder);
    }

    /**
     * @see quantasma.core.BaseTradeStrategy.Builder
     */
    public static class Builder<T extends Builder<T, R>, R extends RSIStrategyGrandChild> extends RSIStrategyChild.Builder<T, R> {

        public Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule, Values<?> parameterValues) {
            super(context, tradeSymbol, entryRule, exitRule, parameterValues);
        }

        public T withGrandChild() {
            return self();
        }

        @Override
        protected T self() {
            return (T) this;
        }

        @Override
        public R build() {
            return (R) new RSIStrategyGrandChild(this);
        }
    }

    public static void main(String[] args) {
        final Context context = new BaseContext.Builder().build();

        final RSIStrategyGrandChild example = new RSIStrategyGrandChild.Builder<>(context, "symbol", new EmptyRule(), new EmptyRule(), Values.of(Parameter.class))
                .withName("from mother of all builders")
                .withChild() // Current builder, type preserved
                .withGrandChild()
                .build();
    }

}
