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

    public BaseTradeStrategy(Context context, String name, String tradeSymbol, Rule entryRule, Rule exitRule, int unstablePeriod) {
        super(name, entryRule, exitRule, unstablePeriod);
        this.context = context;
        this.tradeSymbol = tradeSymbol;
    }

    protected BaseTradeStrategy(Builder builder) {
        super(builder.getName(), builder.getEntryRule(), builder.getExitRule(), builder.getUnstablePeriod());
        this.context = builder.getContext();
        this.tradeSymbol = builder.getTradeSymbol();
        this.amount = DoubleNum.valueOf(0);
    }

    @Override
    public TradeStrategy opposite() {
        return new BaseTradeStrategy(context, "opposite(" + getName() + ")", getTradeSymbol(), getExitRule(), getEntryRule(), getUnstablePeriod());
    }

    @Override
    public TradeStrategy and(Strategy strategy) {
        String andName = "and(" + getName() + "," + strategy.getName() + ")";
        int unstable = Math.max(getUnstablePeriod(), strategy.getUnstablePeriod());
        return and(andName, strategy, unstable);
    }

    @Override
    public TradeStrategy and(String name, Strategy strategy, int unstablePeriod) {
        return new BaseTradeStrategy(context, name, getTradeSymbol(), getEntryRule().and(strategy.getEntryRule()), getExitRule().and(strategy.getExitRule()), unstablePeriod);
    }

    @Override
    public TradeStrategy or(Strategy strategy) {
        String orName = "or(" + getName() + "," + strategy.getName() + ")";
        int unstable = Math.max(getUnstablePeriod(), strategy.getUnstablePeriod());
        return or(orName, strategy, unstable);
    }

    @Override
    public TradeStrategy or(String name, Strategy strategy, int unstablePeriod) {
        return new BaseTradeStrategy(context, name, getTradeSymbol(), getEntryRule().or(strategy.getEntryRule()), getExitRule().or(strategy.getExitRule()), unstablePeriod);
    }

    @Override
    public Rule getExitRule() {
        return super.getExitRule();
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
        throw new UnsupportedOperationException();
    }

    protected void setAmount(Num amount) {
        this.amount = amount;
    }

    @Getter(value = AccessLevel.PROTECTED)
    public static class Builder<T extends Builder<T>> {
        private final Context context;
        private final String tradeSymbol;
        private final Rule entryRule;
        private final Rule exitRule;

        private String name = "unamed_series";
        private int unstablePeriod;

        protected Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule) {
            this.context = Objects.requireNonNull(context);
            this.tradeSymbol = Objects.requireNonNull(tradeSymbol);
            this.entryRule = Objects.requireNonNull(entryRule);
            this.exitRule = Objects.requireNonNull(exitRule);
        }

        // every subclass has to implement it
        protected T self() {
            return (T) this;
        }

        public T withName(String name) {
            this.name = Objects.requireNonNull(name);
            return self();
        }

        public T withUnstablePeriod(int unstablePeriod) {
            this.unstablePeriod = unstablePeriod;
            return self();
        }

        public BaseTradeStrategy build() {
            return new BaseTradeStrategy(this);
        }
    }
}
