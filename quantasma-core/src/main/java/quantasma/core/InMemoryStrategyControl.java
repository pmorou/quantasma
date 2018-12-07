package quantasma.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InMemoryStrategyControl implements StrategyControl {

    private final Map<Long, ControlledTradeStrategy> strategies = new HashMap<>();

    @Override
    public void register(TradeStrategy strategy) {
        if (isAlreadyRegistered(strategy)) {
            throw new RuntimeException("Strategy already registered.");
        }
        strategies.put(generateId(), new ControlledTradeStrategy(strategy, true));
    }

    private boolean isAlreadyRegistered(TradeStrategy strategy) {
        return strategies.entrySet()
                         .stream()
                         .anyMatch(entry -> entry.getValue()
                                                 .getStrategy()
                                                 .equals(strategy));
    }

    private static long generateId() {
        return (long) (Math.random() * Long.MAX_VALUE);
    }

    @Override
    public Set<StrategyInfo> registeredStrategies() {
        return strategies.entrySet()
                         .stream()
                         .map(entry -> new StrategyInfo(entry.getValue().getStrategy().getName(),
                                                        entry.getValue().isActive()))
                         .collect(Collectors.toSet());
    }

    @Override
    public Set<TradeStrategy> activeStrategies() {
        return strategies.entrySet()
                         .stream()
                         .filter(entry -> entry.getValue().isActive())
                         .map(entry -> entry.getValue().getStrategy())
                         .collect(Collectors.toSet());
    }

    @Override
    public void disable(Long id) {
        final ControlledTradeStrategy value = strategies.get(id);
        if (value == null) {
            throw new RuntimeException(String.format("Strategy does not exist: [id: %s]", id));
        }
        value.setActive(false);
    }

    @Override
    public void enable(Long id) {
        final ControlledTradeStrategy value = strategies.get(id);
        if (value == null) {
            throw new RuntimeException(String.format("Strategy does not exist: [id: %s]", id));
        }
        value.setActive(true);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class ControlledTradeStrategy {
        private TradeStrategy strategy;
        private boolean active;
    }
}
