package quantasma.core.timeseries;

import lombok.Getter;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.num.Num;
import quantasma.core.BarPeriod;
import quantasma.core.DateUtils;
import quantasma.core.timeseries.bar.BaseBidAskBar;
import quantasma.core.timeseries.bar.BidAskBar;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class BaseMultipleTimeSeries implements MultipleTimeSeries {
    private static final long serialVersionUID = -8768456438053526527L;

    @Getter
    private final String symbol;
    @Getter
    private final MainTimeSeries mainTimeSeries;
    private final Map<BarPeriod, TypedTimeSeries<BidAskBar>> periodTimeSeriesMap;
    private final UnaryOperator<TimeSeries> wrapper;

    private BaseMultipleTimeSeries(String symbol, TimeSeriesDefinition timeSeriesDefinition, UnaryOperator<TimeSeries> wrapper) {
        this.symbol = symbol;
        this.wrapper = wrapper;
        this.mainTimeSeries = BaseMainTimeSeries.create(timeSeriesDefinition, symbol);
        this.periodTimeSeriesMap = createPeriodTimeSeriesMap(timeSeriesDefinition.getBarPeriod());
    }

    public static BaseMultipleTimeSeries create(String symbol, TimeSeriesDefinition timeSeriesDefinition, UnaryOperator<TimeSeries> wrapper) {
        return new BaseMultipleTimeSeries(symbol, timeSeriesDefinition, wrapper);
    }

    public static BaseMultipleTimeSeries create(String symbol, TimeSeriesDefinition timeSeriesDefinition) {
        return new BaseMultipleTimeSeries(symbol, timeSeriesDefinition, UnaryOperator.identity());
    }

    private Map<BarPeriod, TypedTimeSeries<BidAskBar>> createPeriodTimeSeriesMap(BarPeriod barPeriod) {
        final Map<BarPeriod, TypedTimeSeries<BidAskBar>> timeSeriesMap = new TreeMap<>(Comparator.comparing(BarPeriod::getPeriod)); // first period should save value first
        timeSeriesMap.put(barPeriod, TypedTimeSeries.create(BidAskBar.class, wrap(mainTimeSeries)));
        return timeSeriesMap;
    }

    @Override
    public MultipleTimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition) {
        final AggregatedTimeSeries timeSeries = mainTimeSeries.aggregate(timeSeriesDefinition);
        periodTimeSeriesMap.put(timeSeriesDefinition.getBarPeriod(), TypedTimeSeries.create(BidAskBar.class, wrap(timeSeries)));
        return this;
    }

    private TimeSeries wrap(TimeSeries timeSeries) {
        return wrapper.apply(timeSeries);
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
    public TimeSeries getTimeSeries(BarPeriod period) {
        return periodTimeSeriesMap.get(period).getTimeSeries();
    }

    @Override
    public List<TimeSeries> getTimeSeries() {
        return periodTimeSeriesMap.entrySet().stream()
                                  .map(barPeriodTypedTimeSeriesEntry -> barPeriodTypedTimeSeriesEntry.getValue().getTimeSeries())
                                  .collect(Collectors.toList());
    }

}
