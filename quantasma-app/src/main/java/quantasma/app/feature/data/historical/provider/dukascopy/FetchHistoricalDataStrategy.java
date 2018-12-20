package quantasma.app.feature.data.historical.provider.dukascopy;

import com.dukascopy.api.Filter;
import com.dukascopy.api.IAccount;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IConsole;
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
import quantasma.app.model.OhlcvTick;
import quantasma.app.model.PushTicksSettings;
import quantasma.app.service.OhlcvTickService;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class FetchHistoricalDataStrategy implements IStrategy {
    private final OhlcvTickService ohlcvTickService;
    private final PushTicksSettings pushTicksSettings;

    private IConsole console;
    private IHistory history;
    private boolean isDone;

    public FetchHistoricalDataStrategy(OhlcvTickService ohlcvTickService, PushTicksSettings pushTicksSettings) {
        this.ohlcvTickService = ohlcvTickService;
        this.pushTicksSettings = pushTicksSettings;
    }

    public void onStart(IContext context) throws JFException {
        this.console = context.getConsole();
        this.history = context.getHistory();
        log.info("Strategy started");

        final Instant earliestDate = pushTicksSettings.getFromDate();
        final long startTimeOfCurrentBar = history.getStartTimeOfCurrentBar(resolveInstrument(), resolvePeriod());

        Instant latestDate = Instant.ofEpochMilli(startTimeOfCurrentBar);

        while (!reachedBottom(latestDate, earliestDate)) {
            log.info("Fetching 100_000 bars created <=" + latestDate.atZone(ZoneOffset.UTC));
            final List<IBar> bidBars = history.getBars(resolveInstrument(), resolvePeriod(), OfferSide.BID, Filter.WEEKENDS, 50_000, latestDate.toEpochMilli(), 0);
            final List<IBar> askBars = history.getBars(resolveInstrument(), resolvePeriod(), OfferSide.ASK, Filter.WEEKENDS, 50_000, latestDate.toEpochMilli(), 0);

            final BarBucketCollection collection = new BarBucketCollection();
            bidBars.forEach(collection::insertBidBar);
            askBars.forEach(collection::insertAskBar);
            collection.verify();

            for (Map.Entry<Long, BarBucketCollection.BarBucket> bucketEntry : collection.bars.entrySet()) {
                final BarBucketCollection.BarBucket bucket = bucketEntry.getValue();
                ohlcvTickService.insertSkipDuplicates(new OhlcvTick(pushTicksSettings.getBarPeriod(),
                                                                    Instant.ofEpochMilli(bucketEntry.getKey()),
                                                                    pushTicksSettings.getSymbol(),
                                                                    bucket.bidBar.getOpen(),
                                                                    bucket.bidBar.getLow(),
                                                                    bucket.bidBar.getHigh(),
                                                                    bucket.bidBar.getClose(),
                                                                    bucket.askBar.getOpen(),
                                                                    bucket.askBar.getLow(),
                                                                    bucket.askBar.getHigh(),
                                                                    bucket.askBar.getClose(),
                                                                    (int) (bucket.bidBar.getVolume() + bucket.askBar.getVolume())));
            }

            if (bidBars.size() == 0 || bidBars.get(1).getTime() == latestDate.toEpochMilli()) {
                break; // no more records
            }
            latestDate = Instant.ofEpochMilli(bidBars.get(1).getTime());
        }
        isDone = true;
    }

    private Period resolvePeriod() {
        switch (pushTicksSettings.getBarPeriod()) {
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
        throw new IllegalArgumentException("Unsupported period: [" + pushTicksSettings.getBarPeriod() + "]");
    }

    private Instrument resolveInstrument() {
        return Instrument.valueOf(pushTicksSettings.getSymbol());
    }

    private boolean reachedBottom(Instant latestDate, Instant earliestDate) {
        return latestDate.isBefore(earliestDate);
    }

    private class BarBucketCollection {
        class BarBucket {
            IBar bidBar;
            IBar askBar;
        }

        Map<Long, BarBucket> bars = new TreeMap<>();

        void insertBidBar(IBar bar) {
            final BarBucket nullIfAbsent = bars.computeIfPresent(bar.getTime(), (time, barBucket) -> {
                barBucket.bidBar = bar;
                return barBucket;
            });
            if (nullIfAbsent == null) {
                BarBucket barBucket = new BarBucket();
                barBucket.bidBar = bar;
                bars.putIfAbsent(bar.getTime(), barBucket);
            }
        }

        void insertAskBar(IBar bar) {
            final BarBucket nullIfAbsent = bars.computeIfPresent(bar.getTime(), (aLong, barBucket) -> {
                barBucket.askBar = bar;
                return barBucket;
            });
            if (nullIfAbsent == null) {
                BarBucket barBucket = new BarBucket();
                barBucket.askBar = bar;
                bars.putIfAbsent(bar.getTime(), barBucket);
            }
        }

        void verify() {
            bars.forEach((aLong, barBucket) -> {
                if (barBucket.bidBar == null || barBucket.askBar == null) {
                    throw new RuntimeException();
                }
            });
        }
    }

    public boolean isDone() {
        return isDone;
    }

    public void onAccount(IAccount account) throws JFException {
    }

    public void onMessage(IMessage message) throws JFException {
    }

    public void onStop() throws JFException {
        log.info("Strategy stopped");
    }

    public void onTick(Instrument instrument, ITick tick) throws JFException {
    }

    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
    }

    public void print(String message) {
        console.getOut().println(message);
    }

}