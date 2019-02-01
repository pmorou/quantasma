package quantasma.app.model;

import lombok.Data;
import quantasma.core.analysis.StrategyBacktest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BacktestScenario {
    private final String name;
    private final String strategy;
    private final List<ParameterDescription> parameters;

    public static BacktestScenario from(StrategyBacktest strategyBacktest) {
        return new BacktestScenario(
                strategyBacktest.getClass().getSimpleName(),
                strategyBacktest.strategy().getSimpleName(),
                Arrays.stream(strategyBacktest.parameterizables())
                      .map(parameter -> new ParameterDescription(parameter.name(), parameter.clazz().getSimpleName()))
                      .collect(Collectors.toList()));
    }
}