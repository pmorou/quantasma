package quantasma.app.repository;

import quantasma.app.model.HistoricalDataSummary;
import quantasma.app.model.PersistentOhlcvTick;

import java.time.Instant;
import java.util.List;

public interface HistoricalDataRepository {
    List<PersistentOhlcvTick> findBySymbolAndDateBetweenOrderByDate(String symbol, Instant timeGTE, Instant timeLS);

    PersistentOhlcvTick insert(PersistentOhlcvTick ohlcvTick);

    long countBySymbol(String symbol);

    List<HistoricalDataSummary> dataSummary();
}
