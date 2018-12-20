package quantasma.app.service;

import quantasma.app.model.HistoricalDataSummary;
import quantasma.app.model.OhlcvTick;
import quantasma.app.model.PersistentOhlcvTick;
import quantasma.core.BarPeriod;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;

public interface HistoricalDataService {
    void insert(OhlcvTick ohlcvTick, BarPeriod barPeriod, String symbol);

    void insert(PersistentOhlcvTick persistentOhlcvTick);

    void insertSkipDuplicates(OhlcvTick ohlcvTick);

    List<OhlcvTick> findBySymbolAndDateBetweenOrderByDate(String symbol, Instant startDate, TemporalAmount window);

    long countBySymbol(String symbol);

    List<HistoricalDataSummary> dataSummary();
}
