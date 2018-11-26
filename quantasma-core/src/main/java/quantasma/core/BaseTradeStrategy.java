package quantasma.core;

import lombok.AccessLevel;
import lombok.Getter;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

import java.util.Objects;

public class BaseTradeStrategy extends BaseStrategy implements TradeStrategy {
    private final Context context;

    private String tradeSymbol;
    private Num amount;

    protected BaseTradeStrategy(Builder builder) {
        super(builder.getName(), builder.getEntryRule(), builder.getExitRule(), builder.getUnstablePeriod());
        this.context = Objects.requireNonNull(builder.getContext());
        this.tradeSymbol = Objects.requireNonNull(builder.getTradeSymbol());
        this.amount = DoubleNum.valueOf(0);
    }

    @Override
    public TradeStrategy opposite() {
        return new Builder<>(context, tradeSymbol, getExitRule(), getEntryRule())
                .withName("opposite(" + getName() + ")")
                .withUnstablePeriod(getUnstablePeriod())
                .build();
    }

    @Override
    public TradeStrategy and(Strategy strategy) {
        return new Builder<>(context, tradeSymbol, getEntryRule().and(strategy.getEntryRule()), getExitRule().and(strategy.getExitRule()))
                .withName("and(" + getName() + "," + strategy.getName() + ")")
                .withUnstablePeriod(Math.max(getUnstablePeriod(), strategy.getUnstablePeriod()))
                .build();
    }

    @Override
    public TradeStrategy and(String name, Strategy strategy, int unstablePeriod) {
        return new Builder<>(context, tradeSymbol, getEntryRule().and(strategy.getEntryRule()), getExitRule().and(strategy.getExitRule()))
                .withName(name)
                .withUnstablePeriod(unstablePeriod)
                .build();
    }

    @Override
    public TradeStrategy or(Strategy strategy) {
        return new Builder<>(context, tradeSymbol, getEntryRule().or(strategy.getEntryRule()), getExitRule().or(strategy.getExitRule()))
                .withName("or(" + getName() + "," + strategy.getName() + ")")
                .withUnstablePeriod(Math.max(getUnstablePeriod(), strategy.getUnstablePeriod()))
                .build();
    }

    @Override
    public TradeStrategy or(String name, Strategy strategy, int unstablePeriod) {
        return new Builder<>(context, tradeSymbol, getEntryRule().or(strategy.getEntryRule()), getExitRule().or(strategy.getExitRule()))
                .withName(name)
                .withUnstablePeriod(unstablePeriod)
                .build();
    }

    public void stop() {
    }

    public void start() {
    }

    protected OrderService getOrderService() {
        return context.getOrderService();
    }

    protected MarketData getMarketData() {
        return context.getDataService().getMarketData();
    }

    @Override
    public Num getAmount() {
        return amount;
    }

    @Override
    public String getTradeSymbol() {
        return tradeSymbol;
    }

    protected void setAmount(Num amount) {
        this.amount = amount;
    }

    /**
     * Example of builder which preserves all methods of its parents<p>
     *
     * @param <T> Builder type
     * @param <R> {@code build()} return type
     */
    @Getter(value = AccessLevel.PROTECTED)
    public static class Builder<T extends Builder<T, R>, R extends BaseTradeStrategy> {
        private final Context context;
        private final String tradeSymbol;
        private final Rule entryRule;
        private final Rule exitRule;

        private String name = "unamed_series";
        private int unstablePeriod;

        public Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule) {
            this.context = Objects.requireNonNull(context);
            this.tradeSymbol = Objects.requireNonNull(tradeSymbol);
            this.entryRule = Objects.requireNonNull(entryRule);
            this.exitRule = Objects.requireNonNull(exitRule);
        }

        public T withName(String name) {
            this.name = Objects.requireNonNull(name);
            return self();
        }

        public T withUnstablePeriod(int unstablePeriod) {
            this.unstablePeriod = unstablePeriod;
            return self();
        }

        /**
         * Every builder subclass should implement this method
         */
        protected T self() {
            return (T) this;
        }

        /**
         * Every builder subclass should implement this method
         */
        public R build() {
            return (R) new BaseTradeStrategy(this);
        }
    }
}
