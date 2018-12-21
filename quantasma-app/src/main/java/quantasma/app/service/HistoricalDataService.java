package quantasma.app.service;

import quantasma.app.model.HistoricalDataSummary;
import quantasma.app.model.OhlcvBar;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;

public interface HistoricalDataService {
    void insert(OhlcvBar ohlcvBar);

    void insertSkipDuplicates(OhlcvBar ohlcvBar);

    List<OhlcvBar> findBySymbolAndDateBetweenOrderByDate(String symbol, Instant startDate, TemporalAmount window);

    long countBySymbol(String symbol);

    List<HistoricalDataSummary> dataSummary();
}
