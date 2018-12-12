package quantasma.core.analysis;

import quantasma.core.TradeStrategy;
import quantasma.core.analysis.parametrize.Parameterizable;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

public interface StrategyBacktest {

    Class<? extends TradeStrategy> strategy();

    List<TradeScenario> run(LocalDateTime from, TemporalAmount window);

    Parameterizable[] parameterizables();
}
