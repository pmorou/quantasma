package quantasma.examples.builders;

import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;
import quantasma.core.BaseContext;
import quantasma.core.Context;
import quantasma.examples.RSIStrategy;

public class RSIStrategyChild extends RSIStrategy {
    protected RSIStrategyChild(Builder builder) {
        super(builder);
    }

    /**
     * @see quantasma.core.BaseTradeStrategy.Builder
     */
    public static class Builder<T extends Builder<T, R>, R extends RSIStrategyChild> extends RSIStrategy.Builder<T, R> {

        public Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule) {
            super(context, tradeSymbol, entryRule, exitRule);
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

    protected static class EmptyRule implements Rule {
        @Override
        public boolean isSatisfied(int index, TradingRecord tradingRecord) {
            return true;
        }
    }

    public static void main(String[] args) {
        final Context context = new BaseContext.Builder().build();

        final RSIStrategyChild example = new RSIStrategyChild.Builder<>(context, "symbol", new EmptyRule(), new EmptyRule())
                .withName("from mother of all builders")
                .withChild() // Current builder, type preserved
                .build();
    }
}
