package quantasma.core;

import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.num.Num;

import java.util.Objects;

public class BaseTradeStrategy extends BaseStrategy implements TradeStrategy {
    private final Context context;

    private Num amount;
    private String tradeSymbol;

    public BaseTradeStrategy(Context context, String name, String tradeSymbol, Rule entryRule, Rule exitRule) {
        this(Objects.requireNonNull(context), name, tradeSymbol, entryRule, exitRule, 0);
    }

    public BaseTradeStrategy(Context context, String name, String tradeSymbol, Rule entryRule, Rule exitRule, int unstablePeriod) {
        super(name, entryRule, exitRule, unstablePeriod);
        this.context = context;
        this.tradeSymbol = tradeSymbol;
    }

    @Override
    public Strategy opposite() {
        return new BaseTradeStrategy(context, "opposite(" + getName() + ")", getTradeSymbol(), getExitRule(), getEntryRule(), getUnstablePeriod());
    }

    @Override
    public Strategy and(String name, Strategy strategy, int unstablePeriod) {
        return new BaseTradeStrategy(context, name, getTradeSymbol(), getEntryRule().and(strategy.getEntryRule()), getExitRule().and(strategy.getExitRule()), unstablePeriod);
    }

    @Override
    public Strategy or(String name, Strategy strategy, int unstablePeriod) {
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
}
