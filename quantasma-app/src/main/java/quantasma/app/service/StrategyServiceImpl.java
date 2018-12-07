package quantasma.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.Strategy;
import quantasma.core.StrategyControl;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StrategyServiceImpl implements StrategyService {

    private final StrategyControl strategyControl;

    @Autowired
    public StrategyServiceImpl(StrategyControl strategyControl) {
        this.strategyControl = strategyControl;
    }

    @Override
    public Set<String> all() {
        return strategyControl.registeredStrategies()
                              .stream()
                              .map(Strategy::getName)
                              .collect(Collectors.toSet());
    }
}
