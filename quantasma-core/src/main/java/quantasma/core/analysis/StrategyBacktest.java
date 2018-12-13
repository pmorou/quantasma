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

    List<TradeScenario> run(Map<String, Object[]> parameters, LocalDateTime from, TemporalAmount window);

}
