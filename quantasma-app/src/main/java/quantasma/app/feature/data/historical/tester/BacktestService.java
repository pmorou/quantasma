package quantasma.app.feature.data.historical.tester;

import quantasma.core.TradeStrategy;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;

public interface BacktestService {
    void testOverPeriod(TradeStrategy strategy, ZonedDateTime from, TemporalAmount timeDuration);
}
