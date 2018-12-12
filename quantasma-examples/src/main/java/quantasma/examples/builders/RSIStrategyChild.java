package quantasma.examples.builders;

import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;
import quantasma.core.BaseContext;
import quantasma.core.Context;
import quantasma.core.analysis.parametrize.Parameter;
import quantasma.core.analysis.parametrize.Parameters;
import quantasma.examples.RSIStrategy;

public class RSIStrategyChild extends RSIStrategy {
    protected RSIStrategyChild(Builder builder) {
        super(builder);
    }

    /**
     * @see quantasma.core.BaseTradeStrategy.Builder
     */
    public static class Builder<T extends Builder<T, R>, R extends RSIStrategyChild> extends RSIStrategy.Builder<T, R> {

        public Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule, Parameters parameters) {
            super(context, tradeSymbol, entryRule, exitRule, parameters);
        }

        public T withChild() {
            return self();
        }

        @Override
        protected T self() {
            return (T) this;
        }

        @Override
        public R build() {
            return (R) new RSIStrategyChild(this);
        }
    }

    public enum ParameterList implements Parameter {
        UNDEFINED(Object.class);

        private final Class<?> clazz;

        ParameterList(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Class<?> clazz() {
            return clazz;
        }
    }

    protected static class EmptyRule implements Rule {
        @Override
        public boolean isSatisfied(int index, TradingRecord tradingRecord) {
            return true;
        }
    }

    public static void main(String[] args) {
        final Context context = new BaseContext.Builder().build();

        final RSIStrategyChild example = new RSIStrategyChild.Builder<>(context, "symbol", new EmptyRule(), new EmptyRule(), Parameters.from(ParameterList.class))
                .withName("from mother of all builders")
                .withChild() // Current builder, type preserved
                .build();
    }
}
