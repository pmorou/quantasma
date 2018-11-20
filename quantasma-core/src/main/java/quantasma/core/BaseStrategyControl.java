package quantasma.core;

import org.ta4j.core.Strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseStrategyControl implements StrategyControl {

    private final Map<Strategy, Boolean> strategies = new HashMap<>();

    @Override
    public void register(Strategy strategy) {
        if (strategies.get(strategy) != null) {
            throw new RuntimeException("Strategy already registered.");
        }
        strategies.put(strategy, true);
    }

    @Override
    public Set<Strategy> getActiveStrategies() {
        return strategies.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    @Override
    public void disable(Strategy strategy) {
        strategies.put(strategy, false);
    }

    @Override
    public void enable(Strategy strategy) {
        strategies.put(strategy, true);
    }
}
