package quantasma.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import quantasma.app.config.service.backtest.CriterionsFactory;
import quantasma.app.model.BacktestRequest;
import quantasma.app.model.BacktestScenario;
import quantasma.core.analysis.BacktestResult;
import quantasma.core.analysis.StrategyBacktest;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/backtest")
@Slf4j
public class BacktestController {

    private final List<StrategyBacktest> backtestList;
    private final CriterionsFactory criterionsFactory;

    @Autowired
    public BacktestController(List<StrategyBacktest> backtestList,
                              CriterionsFactory criterionsFactory) {
        this.backtestList = backtestList;
        this.criterionsFactory = criterionsFactory;
    }

    @RequestMapping("all")
    public List<BacktestScenario> all() {
        return backtestList.stream()
                           .map(BacktestScenario::from)
                           .collect(Collectors.toList());
    }

    @RequestMapping("{name}")
    public BacktestScenario get(@PathVariable String name) {
        return backtestList.stream()
                           .filter(matchBacktest(name))
                           .map(BacktestScenario::from)
                           .findFirst()
                           .orElseThrow(RuntimeException::new);
    }

    @RequestMapping(value = "{name}", method = RequestMethod.POST)
    public List<BacktestResult> test(@PathVariable String name, @RequestBody BacktestRequest request) {
        return backtestList.stream()
                           .filter(matchBacktest(name))
                           .findFirst()
                           .map(strategyBacktest -> strategyBacktest.run(request.parameters(strategyBacktest.parameterizables()),
                                                                         request.criterionNames(),
                                                                         request.getTime().getFrom().atStartOfDay(),
                                                                         request.getTime().getWindowAsPeriod()))
                           .orElseThrow(RuntimeException::new);
    }

    private static Predicate<StrategyBacktest> matchBacktest(String name) {
        return strategyBacktest -> strategyBacktest.getClass().getSimpleName().equalsIgnoreCase(name);
    }

    @RequestMapping("criterions")
    public Set<String> criterions() {
        return criterionsFactory.available();
    }

}
