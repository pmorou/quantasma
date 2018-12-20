package quantasma.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quantasma.core.StrategyControl;
import quantasma.core.StrategyDescription;

import java.util.Set;

@Service
public class StrategyServiceImpl implements StrategyService {

    private final StrategyControl strategyControl;

    @Autowired
    public StrategyServiceImpl(StrategyControl strategyControl) {
        this.strategyControl = strategyControl;
    }

    @Override
    public Set<StrategyDescription> all() {
        return strategyControl.registeredStrategies();
    }

    @Override
    public void activate(Long id) {
        strategyControl.enable(id);
    }

    @Override
    public void deactivate(Long id) {
        strategyControl.disable(id);
    }
}
