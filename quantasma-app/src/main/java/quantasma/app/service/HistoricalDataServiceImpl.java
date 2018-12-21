package quantasma.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import quantasma.app.model.OhlcvBar;
import quantasma.app.model.MongoOhlcvBar;
import quantasma.app.model.HistoricalDataSummary;
import quantasma.app.repository.HistoricalDataRepository;
import quantasma.app.util.Util;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HistoricalDataServiceImpl implements HistoricalDataService {

    private final HistoricalDataRepository historicalDataRepository;

    @Autowired
    public HistoricalDataServiceImpl(HistoricalDataRepository historicalDataRepository) {
        this.historicalDataRepository = historicalDataRepository;
    }

    @Override
    public void insert(OhlcvBar ohlcvBar) {
        historicalDataRepository.insert(new MongoOhlcvBar(ohlcvBar.getPeriod(),
                                                          ohlcvBar.getDate(),
                                                          ohlcvBar.getSymbol(),
                                                          ohlcvBar.getBidOpen(),
                                                          ohlcvBar.getBidLow(),
                                                          ohlcvBar.getBidHigh(),
                                                          ohlcvBar.getBidClose(),
                                                          ohlcvBar.getAskOpen(),
                                                          ohlcvBar.getAskLow(),
                                                          ohlcvBar.getAskHigh(),
                                                          ohlcvBar.getAskClose(),
                                                          ohlcvBar.getVolume()));
    }

    @Override
    public void insertSkipDuplicates(OhlcvBar ohlcvBar) {
        final MongoOhlcvBar persistentOhlcvBar = MongoOhlcvBar.from(ohlcvBar);
        try {
            historicalDataRepository.insert(persistentOhlcvBar);
        } catch (DuplicateKeyException e) {
            log.warn("Exception [{}] while inserting [{}]", e.getMessage(), persistentOhlcvBar);
        }
    }

    @Override
    public List<OhlcvBar> findBySymbolAndDateBetweenOrderByDate(String symbol, Instant startDate, TemporalAmount window) {
        return historicalDataRepository.findBySymbolAndDateBetweenOrderByDate(symbol, startDate, Util.instantPlusTemporalAmount(startDate, window))
                                       .stream()
                                       .map(candlestick -> new OhlcvBar(candlestick.getPeriod(),
                                                                        candlestick.getDate(),
                                                                        candlestick.getSymbol(),
                                                                        candlestick.getBidOpen(),
                                                                        candlestick.getBidLow(),
                                                                        candlestick.getBidHigh(),
                                                                        candlestick.getBidClose(),
                                                                        candlestick.getAskOpen(),
                                                                        candlestick.getAskLow(),
                                                                        candlestick.getAskHigh(),
                                                                        candlestick.getAskClose(),
                                                                        candlestick.getVolume()))
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
