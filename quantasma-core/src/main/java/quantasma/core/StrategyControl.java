package quantasma.core;

import java.util.Set;

public interface StrategyControl {
    void register(TradeStrategy tradeStrategy);

    Set<TradeStrategy> registeredStrategies();

    Set<TradeStrategy> getActiveStrategies();

    void disable(TradeStrategy tradeStrategy);

    void enable(TradeStrategy tradeStrategy);
}
