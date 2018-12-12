package quantasma.app.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quantasma.core.analysis.StrategyBacktest;
import quantasma.core.analysis.parametrize.Parameterizable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backtest")
public class BacktestController {

    private final List<StrategyBacktest> backtestList;

    @Autowired
    public BacktestController(List<StrategyBacktest> backtestList) {
        this.backtestList = backtestList;
    }

    @RequestMapping("all")
    public List<BacktestScenario> all() {
        return backtestList.stream()
                           .map(backtest -> new BacktestScenario(
                                   backtest.strategy().getSimpleName(),
                                   Arrays.stream(backtest.parameterizables())
                                         .collect(Collectors.toMap(Parameterizable::name,
                                                                   o -> o.clazz().getSimpleName()))))
                           .collect(Collectors.toList());
    }

    @Data
    static class BacktestScenario {
        private final String strategy;
        private final Map<String, String> parameterizable;
    }
}
