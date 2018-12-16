package quantasma.app.model;

import lombok.Data;

import java.util.List;

@Data
public class BacktestScenario {
    private final String name;
    private final String strategy;
    private final List<ParameterDescription> parameters;
}