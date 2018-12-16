package quantasma.core.analysis;

import quantasma.core.TradeStrategy;
import quantasma.core.analysis.parametrize.Parameterizable;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Map;

public interface StrategyBacktest {

    Class<? extends TradeStrategy> strategy();

    Parameterizable[] parameterizables();

    List<BacktestResult> run(Map<String, Object[]> parameters, List<String> criterions, LocalDateTime from, TemporalAmount window);

}
