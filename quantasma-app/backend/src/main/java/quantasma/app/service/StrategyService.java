package quantasma.app.service;

import quantasma.core.StrategyDescription;

import java.util.Set;

public interface StrategyService {
    Set<StrategyDescription> all();

    void activate(Long id);

    void deactivate(Long id);
}
