package quantasma.core;

import java.util.Set;

public interface StrategyControl {
    void register(TradeStrategy tradeStrategy);

    Set<StrategyInfo> registeredStrategies();

    Set<TradeStrategy> activeStrategies();

    void disable(Long id);

    void enable(Long id);
}
