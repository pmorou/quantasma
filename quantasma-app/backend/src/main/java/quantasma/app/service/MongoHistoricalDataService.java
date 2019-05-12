package quantasma.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import quantasma.app.model.HistoricalDataSummary;
import quantasma.app.model.MongoOhlcvBar;
import quantasma.app.model.OhlcvBar;
import quantasma.app.repository.HistoricalDataRepository;
import quantasma.app.util.Util;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MongoHistoricalDataService implements HistoricalDataService {

    private final HistoricalDataRepository historicalDataRepository;

    @Autowired
    public MongoHistoricalDataService(HistoricalDataRepository historicalDataRepository) {
        this.historicalDataRepository = historicalDataRepository;
    }

    @Override
    public void insert(OhlcvBar ohlcvBar) {
        historicalDataRepository.insert(MongoOhlcvBar.from(ohlcvBar));
    }

    @Override
    public void insertSkipDuplicates(OhlcvBar ohlcvBar) {
        final MongoOhlcvBar mongoOhlcvBar = MongoOhlcvBar.from(ohlcvBar);
        try {
            historicalDataRepository.insert(mongoOhlcvBar);
        } catch (DuplicateKeyException e) {
            log.warn("Exception [{}] while inserting [{}]", e.getMessage(), mongoOhlcvBar);
        }
    }

    @Override
    public List<OhlcvBar> findBySymbolAndDateBetweenOrderByDate(String symbol, Instant startDate, TemporalAmount window) {
        return historicalDataRepository.findBySymbolAndDateBetweenOrderByDate(symbol, startDate, Util.instantPlusTemporalAmount(startDate, window))
            .stream()
            .map(MongoOhlcvBar::toOhlcvBar)
            .collect(Collectors.toList());
    }

    @Override
    public long countBySymbol(String symbol) {
        return historicalDataRepository.countBySymbol(symbol);
    }

    @Override
    public List<HistoricalDataSummary> dataSummary() {
        return historicalDataRepository.dataSummary();
    }

}
