package quantasma.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import quantasma.app.config.service.backtest.CriterionsFactory;
import quantasma.app.model.BacktestRequest;
import quantasma.app.model.BacktestScenario;
import quantasma.core.analysis.BacktestResult;
import quantasma.core.analysis.StrategyBacktest;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BacktestServiceImpl implements BacktestService {

    private final List<StrategyBacktest> backtests;
    private final CriterionsFactory criterionsFactory;

    @Autowired
    public BacktestServiceImpl(List<StrategyBacktest> backtests,
                               CriterionsFactory criterionsFactory) {
        this.backtests = backtests;
        this.criterionsFactory = criterionsFactory;
    }

    @Override
    public List<BacktestScenario> all() {
        return backtests.stream()
                        .map(BacktestScenario::from)
                        .collect(Collectors.toList());
    }

    @Override
    public BacktestScenario get(String name) {
        return backtests.stream()
                        .filter(matchBacktest(name))
                        .map(BacktestScenario::from)
                        .findFirst()
                        .orElseThrow(unknownBacktest(name));
    }

    private Supplier<IllegalArgumentException> unknownBacktest(@PathVariable String name) {
        return () -> new IllegalArgumentException(String.format("Unknown backtest: %s", name));
    }

    @Override
    public List<BacktestResult> test(String name, BacktestRequest request) {
        return backtests.stream()
                        .filter(matchBacktest(name))
                        .findFirst()
                        .map(strategyBacktest -> strategyBacktest.run(request.parameters(strategyBacktest.parameterizables()),
                                                                      request.criterionNames(),
                                                                      request.getTime().getFrom().atStartOfDay(),
                                                                      request.getTime().getWindowAsPeriod()))
                        .orElseThrow(unknownBacktest(name));
    }

    private static Predicate<StrategyBacktest> matchBacktest(String name) {
        return strategyBacktest -> strategyBacktest.getClass().getSimpleName().equalsIgnoreCase(name);
    }

    @Override
    public Set<String> criterions() {
        return criterionsFactory.available();
    }
}
