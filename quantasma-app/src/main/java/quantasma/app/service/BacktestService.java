package quantasma.app.service;

import quantasma.app.model.BacktestRequest;
import quantasma.app.model.BacktestScenario;
import quantasma.core.analysis.BacktestResult;

import java.util.List;
import java.util.Set;

public interface BacktestService {

    List<BacktestScenario> all();

    BacktestScenario get(String name);

    List<BacktestResult> test(String name, BacktestRequest request);

    Set<String> criterions();
}
