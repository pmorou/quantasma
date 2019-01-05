package quantasma.app.feature.data.historical.provider.dukascopy;

import com.dukascopy.api.Filter;
import com.dukascopy.api.IAccount;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IHistory;
import com.dukascopy.api.IMessage;
import com.dukascopy.api.IStrategy;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.Period;
import lombok.extern.slf4j.Slf4j;
import quantasma.app.model.FeedBarsSettings;
import quantasma.app.service.HistoricalDataService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class FetchHistoricalDataStrategy implements IStrategy {
    private final HistoricalDataService historicalDataService;
    private final FeedBarsSettings feedBarsSettings;

    private IHistory history;
    private boolean isDone;

    public FetchHistoricalDataStrategy(HistoricalDataService historicalDataService, FeedBarsSettings feedBarsSettings) {
        this.historicalDataService = historicalDataService;
        this.feedBarsSettings = feedBarsSettings;
    }

    @Override
    public void onStart(IContext context) throws JFException {
        log.info("Strategy started");
        this.history = context.getHistory();

        Instant fetchFrom = feedBarsSettings.getFromDate();
        Instant fetchTo = getValidFetchTo();

        if (fetchTo.isBefore(fetchFrom)) {
            throw new IllegalArgumentException(String.format("From date [%s] > to date [%s]", fetchFrom, fetchTo));
        }

        while (!fetchTo.equals(feedBarsSettings.getFromDate())) {
            fetchFrom = rollWindow(fetchTo);
            log.info("Fetching bars - from: [{}], to: [{}]", fetchFrom, fetchTo);

            final List<IBar> bidBars = fetchBars(fetchFrom, fetchTo, OfferSide.BID);
            final List<IBar> askBars = fetchBars(fetchFrom, fetchTo, OfferSide.ASK);
            final BarsCollection bars = new BarsCollection(feedBarsSettings.getSymbol(), feedBarsSettings.getBarPeriod());
            bidBars.forEach(bars::insertBidBar);
            askBars.forEach(bars::insertAskBar);
            bars.verifyIntegrity();
            bars.getBars().forEach((key, value) -> historicalDataService.insertSkipDuplicates(value.toOhlcv()));

            if (bidBars.isEmpty() || askBars.isEmpty()) {
                log.info("No bar found for given date range, stopping fetch");
                break;
            }
            fetchTo = fetchFrom;
        }
        isDone = true;
    }

    private List<IBar> fetchBars(Instant fetchFrom, Instant fetchTo, OfferSide bid) throws JFException {
        return history.getBars(resolveInstrument(), resolvePeriod(), bid, Filter.WEEKENDS, fetchFrom.toEpochMilli(), fetchTo.toEpochMilli());
    }

    private Instant rollWindow(Instant fetchTo) {
        return Instant.ofEpochMilli(Math.max(feedBarsSettings.getFromDate().toEpochMilli(),
                                             fetchTo.minus(60, ChronoUnit.DAYS).toEpochMilli()));
    }

    private Instant getValidFetchTo() throws JFException {
        final long latestPossibleBar = history.getStartTimeOfCurrentBar(resolveInstrument(), resolvePeriod());
        return Instant.ofEpochMilli(Math.min(latestPossibleBar,
                                             feedBarsSettings.getToDate().toEpochMilli()));
    }

    private Period resolvePeriod() {
        switch (feedBarsSettings.getBarPeriod()) {
            case M1:
                return Period.ONE_MIN;
            case M5:
                return Period.FIVE_MINS;
            case M15:
                return Period.FIFTEEN_MINS;
            case M30:
                return Period.THIRTY_MINS;
            case H1:
                return Period.ONE_HOUR;
            case H4:
                return Period.FOUR_HOURS;
            case D:
                return Period.ONE_HOUR;
        }
        throw new IllegalArgumentException("Unsupported period: [" + feedBarsSettings.getBarPeriod() + "]");
    }

    private Instrument resolveInstrument() {
        return Instrument.valueOf(feedBarsSettings.getSymbol());
    }

    public boolean isDone() {
        return isDone;
    }

    @Override
    public void onAccount(IAccount account) {
    }

    @Override
    public void onMessage(IMessage message) {
    }

    @Override
    public void onStop() {
        log.info("Strategy stopped");
    }

    @Override
    public void onTick(Instrument instrument, ITick tick) {
    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) {
    }

}