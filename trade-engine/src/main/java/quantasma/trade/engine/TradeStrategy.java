package quantasma.trade.engine;

import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;

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
    public boolean shouldEnter(int index, TradingRecord tradingRecord) {
        final boolean shouldEnter = super.shouldEnter(index, tradingRecord);
        if (shouldEnter) {
            context.getOrderService().openPosition(null);
        }
        return shouldEnter;
    }

    @Override
    public boolean shouldExit(int index, TradingRecord tradingRecord) {
        final boolean shouldExit = super.shouldExit(index, tradingRecord);
        if (shouldExit) {
            context.getOrderService().closePosition(null);
        }
        return shouldExit;
    }

    @Override
    public Rule getExitRule() {
        return super.getExitRule();
    }

    public void stop() {
    }

    public void start() {

    }
}
