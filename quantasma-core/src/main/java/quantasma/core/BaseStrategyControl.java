package quantasma.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseStrategyControl implements StrategyControl {

    private final Map<TradeStrategy, Boolean> strategies = new HashMap<>();

    @Override
    public void register(TradeStrategy strategy) {
        if (strategies.get(strategy) != null) {
            throw new RuntimeException("Strategy already registered.");
        }
        strategies.put(strategy, true);
    }

    @Override
    public Set<TradeStrategy> registeredStrategies() {
        return strategies.entrySet()
                         .stream()
                         .map(Map.Entry::getKey)
                         .collect(Collectors.toSet());
    }

    @Override
    public Set<TradeStrategy> activeStrategies() {
        return strategies.entrySet()
                         .stream()
                         .filter(Map.Entry::getValue)
                         .map(Map.Entry::getKey)
                         .collect(Collectors.toSet());
    }

    @Override
    public void disable(TradeStrategy strategy) {
        strategies.put(strategy, false);
    }

    @Override
    public void enable(TradeStrategy strategy) {
        strategies.put(strategy, true);
    }
}
