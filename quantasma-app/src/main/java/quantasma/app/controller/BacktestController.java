package quantasma.app.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quantasma.app.config.service.backtest.CriterionsFactory;
import quantasma.core.analysis.StrategyBacktest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backtest")
public class BacktestController {

    private final List<StrategyBacktest> backtestList;
    private final CriterionsFactory criterionsFactory;

    @Autowired
    public BacktestController(List<StrategyBacktest> backtestList, CriterionsFactory criterionsFactory) {
        this.backtestList = backtestList;
        this.criterionsFactory = criterionsFactory;
    }

    @RequestMapping("all")
    public List<BacktestScenario> all() {
        return backtestList.stream()
                           .map(backtest -> new BacktestScenario(
                                   backtest.getClass().getSimpleName(),
                                   backtest.strategy().getSimpleName(),
                                   Arrays.stream(backtest.parameterizables())
                                         .map(p -> new Parameter(
                                                 p.name(),
                                                 p.clazz().getSimpleName()))
                                         .collect(Collectors.toList())))
                           .collect(Collectors.toList());
    }

    @Data
    static class BacktestScenario {
        private final String name;
        private final String strategy;
        private final List<Parameter> parameters;
    }

    @Data
    static class Parameter {
        private final String name;
        private final String type;
    }

    @RequestMapping("{name}")
    public BacktestScenario get(@PathVariable String name) {
        return backtestList.stream()
                           .filter(strategyBacktest -> strategyBacktest.getClass().getSimpleName().equalsIgnoreCase(name))
                           .map(backtest -> new BacktestScenario(
                                   backtest.getClass().getSimpleName(),
                                   backtest.strategy().getSimpleName(),
                                   Arrays.stream(backtest.parameterizables())
                                         .map(p -> new Parameter(
                                                 p.name(),
                                                 p.clazz().getSimpleName()))
                                         .collect(Collectors.toList())))
                           .findFirst()
                           .orElseThrow(RuntimeException::new);
    }

    @RequestMapping("criterions")
    public Set<String> criterions() {
        return criterionsFactory.available();
    }
}
