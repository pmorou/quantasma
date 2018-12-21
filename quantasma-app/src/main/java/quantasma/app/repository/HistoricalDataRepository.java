package quantasma.app.repository;

import quantasma.app.model.HistoricalDataSummary;
import quantasma.app.model.MongoOhlcvBar;

import java.time.Instant;
import java.util.List;

public interface HistoricalDataRepository {
    List<MongoOhlcvBar> findBySymbolAndDateBetweenOrderByDate(String symbol, Instant timeGTE, Instant timeLS);

    MongoOhlcvBar insert(MongoOhlcvBar ohlcvBar);

    long countBySymbol(String symbol);

    List<HistoricalDataSummary> dataSummary();
}
