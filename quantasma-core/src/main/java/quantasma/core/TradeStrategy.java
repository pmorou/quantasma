package quantasma.core;

import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;

public class TradeStrategy extends BaseStrategy {
    private final Context context;

    public TradeStrategy(Context context, String name, Rule entryRule, Rule exitRule) {
        this(context, name, entryRule, exitRule, 0);
    }

    public TradeStrategy(Context context, String name, Rule entryRule, Rule exitRule, int unstablePeriod) {
        super(name, entryRule, exitRule, unstablePeriod);
        this.context = context;
    }

    @Override
    public Strategy opposite() {
        return new TradeStrategy(context, "opposite(" + getName() + ")", getExitRule(), getEntryRule(), getUnstablePeriod());
    }

    @Override
    public Strategy and(String name, Strategy strategy, int unstablePeriod) {
        return new TradeStrategy(context, name, getEntryRule().and(strategy.getEntryRule()), getExitRule().and(strategy.getExitRule()), unstablePeriod);
    }

    @Override
    public Strategy or(String name, Strategy strategy, int unstablePeriod) {
        return new TradeStrategy(context, name, getEntryRule().or(strategy.getEntryRule()), getExitRule().or(strategy.getExitRule()), unstablePeriod);
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
}
