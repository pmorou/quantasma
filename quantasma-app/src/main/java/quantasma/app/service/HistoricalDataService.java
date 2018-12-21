package quantasma.app.service;

import quantasma.app.model.HistoricalDataSummary;
import quantasma.app.model.OhlcvBar;
import quantasma.app.model.MongoOhlcvBar;
import quantasma.core.BarPeriod;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;

public interface HistoricalDataService {
    void insert(OhlcvBar ohlcvBar, BarPeriod barPeriod, String symbol);

    void insert(MongoOhlcvBar persistentOhlcvBar);

    void insertSkipDuplicates(OhlcvBar ohlcvBar);

    List<OhlcvBar> findBySymbolAndDateBetweenOrderByDate(String symbol, Instant startDate, TemporalAmount window);

    long countBySymbol(String symbol);

    List<HistoricalDataSummary> dataSummary();
}
