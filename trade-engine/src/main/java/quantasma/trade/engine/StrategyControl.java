package quantasma.trade.engine;

import org.ta4j.core.Strategy;

import java.util.Set;

public interface StrategyControl {
    void register(Strategy strategy);

    Set<Strategy> getActiveStrategies();

    void disable(Strategy strategy);

    void enable(Strategy strategy);
}
