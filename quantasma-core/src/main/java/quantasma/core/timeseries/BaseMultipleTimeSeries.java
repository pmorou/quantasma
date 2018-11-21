package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.num.Num;
import quantasma.core.BarPeriod;
import quantasma.core.DateUtils;
import quantasma.core.timeseries.bar.BaseBidAskBar;
import quantasma.core.timeseries.bar.BidAskBar;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class BaseMultipleTimeSeries implements MultipleTimeSeries {
    private static final long serialVersionUID = -8768456438053526527L;

    private final String instrument;
    private final ReferenceTimeSeries referenceTimeSeries;
    private final Map<BarPeriod, TypedTimeSeries<BidAskBar>> periodTimeSeriesMap;

    private BaseMultipleTimeSeries(String instrument, TimeSeriesDefinition timeSeriesDefinition) {
        this.instrument = instrument;
        this.referenceTimeSeries = BaseReferenceTimeSeries.create(timeSeriesDefinition);
        this.periodTimeSeriesMap = createPeriodTimeSeriesMap(timeSeriesDefinition.getBarPeriod());
    }

    public static BaseMultipleTimeSeries create(String instrument, TimeSeriesDefinition timeSeriesDefinition) {
        return new BaseMultipleTimeSeries(instrument, timeSeriesDefinition);
    }

    private Map<BarPeriod, TypedTimeSeries<BidAskBar>> createPeriodTimeSeriesMap(BarPeriod barPeriod) {
        final Map<BarPeriod, TypedTimeSeries<BidAskBar>> timeSeriesMap = new TreeMap<>(Comparator.comparing(BarPeriod::getPeriod)); // first period should save value first
        timeSeriesMap.put(barPeriod, TypedTimeSeries.create(BidAskBar.class, referenceTimeSeries.source()));
        return timeSeriesMap;
    }

    public MultipleTimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition) {
        final TimeSeries timeSeries = referenceTimeSeries.aggregate(timeSeriesDefinition);
        periodTimeSeriesMap.put(timeSeriesDefinition.getBarPeriod(), TypedTimeSeries.create(BidAskBar.class, timeSeries));
        return this;
    }

    @Override
    public void updateBar(ZonedDateTime priceDate, double bid, double ask) {
        periodTimeSeriesMap.forEach((barPeriod, typedTimeSeries) -> {
            if (empty(typedTimeSeries) || isEqualOrAfter(priceDate, typedTimeSeries.getLastBar().getEndTime())) {
                insertNewBar(priceDate, barPeriod, typedTimeSeries);
            }
            final BidAskBar lastBar = typedTimeSeries.getLastBar();
            lastBar.addPrice(typedTimeSeries.getTimeSeries().numOf(bid), typedTimeSeries.getTimeSeries().numOf(ask));
        });
    }

    @Override
    public void updateBar(ZonedDateTime priceDate, double price) {
        periodTimeSeriesMap.forEach((barPeriod, typedTimeSeries) -> {
            if (empty(typedTimeSeries) || isEqualOrAfter(priceDate, typedTimeSeries.getLastBar().getEndTime())) {
                insertNewBar(priceDate, barPeriod, typedTimeSeries);
            }
            final Bar lastBar = typedTimeSeries.getLastBar();
            lastBar.addPrice(typedTimeSeries.getTimeSeries().numOf(price));
        });
    }

    @Override
    public void createBar(ZonedDateTime priceDate) {
        periodTimeSeriesMap.forEach((barPeriod, typedTimeSeries) -> {
            if (empty(typedTimeSeries)) {
                insertNewBar(priceDate, barPeriod, typedTimeSeries);
            } else if (isEqualOrAfter(priceDate, typedTimeSeries.getLastBar().getEndTime())) {
                insertNewBarWithLastPrice(priceDate, barPeriod, typedTimeSeries);
            }
        });
    }

    private boolean empty(TypedTimeSeries<? extends Bar> typedTimeSeries) {
        return typedTimeSeries.getTimeSeries().getBarCount() == 0;
    }

    private void insertNewBar(ZonedDateTime priceDate, BarPeriod barPeriod, TypedTimeSeries<? super BidAskBar> typedTimeSeries) {
        typedTimeSeries.addBar(new BaseBidAskBar(barPeriod.getPeriod(),
                                                 DateUtils.createEndDate(priceDate, barPeriod),
                                                 typedTimeSeries.getTimeSeries().function()));
    }

    private void insertNewBarWithLastPrice(ZonedDateTime priceDate, BarPeriod barPeriod, TypedTimeSeries<? super BidAskBar> typedTimeSeries) {
        final Num lastPrice = typedTimeSeries.getLastBar().getClosePrice();
        final TimeSeries timeSeries = typedTimeSeries.getTimeSeries();
        timeSeries.addBar(new BaseBidAskBar(barPeriod.getPeriod(),
                                            DateUtils.createEndDate(priceDate, barPeriod),
                                            timeSeries.function()));
        timeSeries.addPrice(lastPrice);
    }

    private static boolean isEqualOrAfter(ZonedDateTime date, ZonedDateTime withThis) {
        return date.equals(withThis) || date.isAfter(withThis);
    }

    @Override
    public int lastBarIndex() {
        return periodTimeSeriesMap.entrySet()
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
    public TimeSeries getTimeSeries(BarPeriod period) {
        return periodTimeSeriesMap.get(period).getTimeSeries();
    }
}
