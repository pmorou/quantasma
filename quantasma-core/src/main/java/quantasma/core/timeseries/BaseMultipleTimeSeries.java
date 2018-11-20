package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.num.Num;
import quantasma.core.CandlePeriod;
import quantasma.core.DateUtils;
import quantasma.core.BaseBidAskBar;
import quantasma.core.BidAskBar;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class BaseMultipleTimeSeries implements MultipleTimeSeries {
    private static final long serialVersionUID = -8768456438053526527L;

    private final Map<CandlePeriod, TypedTimeSeries<BidAskBar>> timeSeriesMap;
    private final String instrument;

    private BaseMultipleTimeSeries(String instrument, Map<CandlePeriod, TypedTimeSeries<BidAskBar>> timeSeriesMap) {
        this.instrument = instrument;
        this.timeSeriesMap = timeSeriesMap;
    }

    public BaseMultipleTimeSeries put(CandlePeriod candlePeriod, TimeSeries timeSeries) {
        timeSeriesMap.put(candlePeriod, TypedTimeSeries.create(BidAskBar.class, timeSeries));
        return this;
    }

    public BaseMultipleTimeSeries(String instrument, TimeSeriesDefinition timeSeriesDefinition) {
        this.instrument = instrument;
        this.timeSeriesMap = createBaseTimeSeries(timeSeriesDefinition);
    }

    private Map<CandlePeriod, TypedTimeSeries<BidAskBar>> createBaseTimeSeries(TimeSeriesDefinition timeSeriesDefinition) {
        final BaseTimeSeriesFactory baseTimeSeriesFactory = new BaseTimeSeriesFactory();
        final Map<CandlePeriod, TypedTimeSeries<BidAskBar>> timeSeriesMap = new TreeMap<>(Comparator.comparing(CandlePeriod::getPeriod)); // first period should save value first
        timeSeriesMap.put(timeSeriesDefinition.getCandlePeriod(),
                          TypedTimeSeries.create(BidAskBar.class, baseTimeSeriesFactory.createInstance(timeSeriesDefinition)));
        return timeSeriesMap;
    }

    public static BaseMultipleTimeSeries create(String instrument, TimeSeriesDefinition timeSeriesDefinition) {
        return new BaseMultipleTimeSeries(instrument, timeSeriesDefinition);
    }

    @Override
    public void updateBar(ZonedDateTime priceDate, double bid, double ask) {
        timeSeriesMap.forEach((candlePeriod, typedTimeSeries) -> {
            if (empty(typedTimeSeries) || isEqualOrAfter(priceDate, typedTimeSeries.getLastBar().getEndTime())) {
                insertNewBar(priceDate, candlePeriod, typedTimeSeries);
            }
            final BidAskBar lastBar = typedTimeSeries.getLastBar();
            lastBar.addPrice(typedTimeSeries.getTimeSeries().numOf(bid), typedTimeSeries.getTimeSeries().numOf(ask));
        });
    }

    @Override
    public void updateBar(ZonedDateTime priceDate, double price) {
        timeSeriesMap.forEach((candlePeriod, typedTimeSeries) -> {
            if (empty(typedTimeSeries) || isEqualOrAfter(priceDate, typedTimeSeries.getLastBar().getEndTime())) {
                insertNewBar(priceDate, candlePeriod, typedTimeSeries);
            }
            final Bar lastBar = typedTimeSeries.getLastBar();
            lastBar.addPrice(typedTimeSeries.getTimeSeries().numOf(price));
        });
    }

    @Override
    public void createBar(ZonedDateTime priceDate) {
        timeSeriesMap.forEach((candlePeriod, typedTimeSeries) -> {
            if (empty(typedTimeSeries)) {
                insertNewBar(priceDate, candlePeriod, typedTimeSeries);
            } else if (isEqualOrAfter(priceDate, typedTimeSeries.getLastBar().getEndTime())) {
                insertNewBarWithLastPrice(priceDate, candlePeriod, typedTimeSeries);
            }
        });
    }

    private boolean empty(TypedTimeSeries<? extends Bar> typedTimeSeries) {
        return typedTimeSeries.getTimeSeries().getBarCount() == 0;
    }

    private void insertNewBar(ZonedDateTime priceDate, CandlePeriod candlePeriod, TypedTimeSeries<? super BidAskBar> typedTimeSeries) {
        typedTimeSeries.addBar(new BaseBidAskBar(candlePeriod.getPeriod(),
                                                 DateUtils.createEndDate(priceDate, candlePeriod),
                                                 typedTimeSeries.getTimeSeries().function()));
    }

    private void insertNewBarWithLastPrice(ZonedDateTime priceDate, CandlePeriod candlePeriod, TypedTimeSeries<? super BidAskBar> typedTimeSeries) {
        final Num lastPrice = typedTimeSeries.getLastBar().getClosePrice();
        final TimeSeries timeSeries = typedTimeSeries.getTimeSeries();
        timeSeries.addBar(new BaseBidAskBar(candlePeriod.getPeriod(),
                                            DateUtils.createEndDate(priceDate, candlePeriod),
                                            timeSeries.function()));
        timeSeries.addPrice(lastPrice);
    }

    private static boolean isEqualOrAfter(ZonedDateTime date, ZonedDateTime withThis) {
        return date.equals(withThis) || date.isAfter(withThis);
    }

    @Override
    public int lastBarIndex() {
        return timeSeriesMap.entrySet()
                            .stream()
                            .findFirst()
                            .map(entry -> entry.getValue()
                                               .getTimeSeries()
                                               .getEndIndex())
                            .orElse(-1);
    }

    @Override
    public String getInstrument() {
        return instrument;
    }

    @Override
    public TimeSeries getTimeSeries(CandlePeriod period) {
        return timeSeriesMap.get(period).getTimeSeries();
    }
}
