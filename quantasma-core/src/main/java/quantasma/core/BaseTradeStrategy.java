package quantasma.core;

import lombok.AccessLevel;
import lombok.Getter;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;
import quantasma.core.analysis.parametrize.Parameterizable;
import quantasma.core.analysis.parametrize.Values;

import java.util.Objects;
import java.util.function.Function;

public class BaseTradeStrategy extends BaseStrategy implements TradeStrategy {
    private final Context context;
    private final Function<Number, Num> numFunction;
    private final Values<?> parameterValues;

    private String tradeSymbol;
    private Num amount;

    protected BaseTradeStrategy(Builder<?, ?> builder) {
        super(builder.getName(), builder.getEntryRule(), builder.getExitRule(), builder.getUnstablePeriod());
        this.context = Objects.requireNonNull(builder.getContext());
        this.tradeSymbol = Objects.requireNonNull(builder.getTradeSymbol());
        this.numFunction = Objects.requireNonNull(builder.getNumFunction());
        this.amount = numFunction.apply(builder.getAmount());
        this.parameterValues = builder.getParametersValues();
    }

    @Override
    public TradeStrategy opposite() {
        return new Builder<>(context, tradeSymbol, getExitRule(), getEntryRule(), parameterValues)
            .withName("opposite(" + getName() + ")")
            .withUnstablePeriod(getUnstablePeriod())
            .build();
    }

    @Override
    public TradeStrategy and(Strategy strategy) {
        return new Builder<>(context, tradeSymbol, getEntryRule().and(strategy.getEntryRule()), getExitRule().and(strategy.getExitRule()), parameterValues)
            .withName("and(" + getName() + "," + strategy.getName() + ")")
            .withUnstablePeriod(Math.max(getUnstablePeriod(), strategy.getUnstablePeriod()))
            .build();
    }

    @Override
    public TradeStrategy and(String name, Strategy strategy, int unstablePeriod) {
        return new Builder<>(context, tradeSymbol, getEntryRule().and(strategy.getEntryRule()), getExitRule().and(strategy.getExitRule()), parameterValues)
            .withName(name)
            .withUnstablePeriod(unstablePeriod)
            .build();
    }

    @Override
    public TradeStrategy or(Strategy strategy) {
        return new Builder<>(context, tradeSymbol, getEntryRule().or(strategy.getEntryRule()), getExitRule().or(strategy.getExitRule()), parameterValues)
            .withName("or(" + getName() + "," + strategy.getName() + ")")
            .withUnstablePeriod(Math.max(getUnstablePeriod(), strategy.getUnstablePeriod()))
            .build();
    }

    @Override
    public TradeStrategy or(String name, Strategy strategy, int unstablePeriod) {
        return new Builder<>(context, tradeSymbol, getEntryRule().or(strategy.getEntryRule()), getExitRule().or(strategy.getExitRule()), parameterValues)
            .withName(name)
            .withUnstablePeriod(unstablePeriod)
            .build();
    }

    public void stop() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void start() {
        throw new UnsupportedOperationException("Not yet implemented");
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

    protected void setAmount(Num amount) {
        this.amount = amount;
    }

    @Override
    public String getTradeSymbol() {
        return tradeSymbol;
    }

    @Override
    public Values<?> getParameterValues() {
        return parameterValues;
    }

    @Override
    public Parameterizable[] parameterizables() {
        return new Parameterizable[0];
    }

    @Override
    public void perform() {
        final int lastBarIndex = getMarketData().lastBarIndex(); // TODO: cache or calculate once
        if (!shouldEnter(lastBarIndex))
            shouldExit(lastBarIndex);
    }

    protected Function<Number, Num> getNumFunction() {
        return numFunction;
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
        private final Values<?> parametersValues;

        private String name = "unamed_series";
        private int unstablePeriod;
        private Function<Number, Num> numFunction = PrecisionNum::valueOf;
        private int amount = 100;

        public Builder(Context context, String tradeSymbol, Rule entryRule, Rule exitRule, Values<?> parameterValues) {
            this.context = Objects.requireNonNull(context);
            this.tradeSymbol = Objects.requireNonNull(tradeSymbol);
            this.entryRule = Objects.requireNonNull(entryRule);
            this.exitRule = Objects.requireNonNull(exitRule);
            this.parametersValues = Objects.requireNonNull(parameterValues);
        }

        public T withName(String name) {
            this.name = Objects.requireNonNull(name);
            return self();
        }

        public T withUnstablePeriod(int unstablePeriod) {
            this.unstablePeriod = unstablePeriod;
            return self();
        }

        public T withNumTypeOf(Function<Number, Num> numFunction) {
            this.numFunction = numFunction;
            return self();
        }

        public T withNumTypeOf(Num type) {
            this.numFunction = type.function();
            return self();
        }

        public T withAmount(int amount) {
            this.amount = amount;
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
