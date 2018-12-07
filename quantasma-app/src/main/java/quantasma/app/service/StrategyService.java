package quantasma.app.service;

import quantasma.core.StrategyInfo;

import java.util.Set;

public interface StrategyService {
    Set<StrategyInfo> all();

    void activate(Long id);

    void deactivate(Long id);
}
