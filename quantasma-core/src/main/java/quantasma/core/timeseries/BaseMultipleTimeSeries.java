package quantasma.core.timeseries;

import lombok.Getter;
import org.ta4j.core.Bar;
import org.ta4j.core.num.Num;
import quantasma.core.BarPeriod;
import quantasma.core.DateUtils;
import quantasma.core.timeseries.bar.BaseBidAskBar;
import quantasma.core.timeseries.bar.BidAskBar;
import quantasma.core.timeseries.bar.factory.BarFactory;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class BaseMultipleTimeSeries<B extends Bar> implements MultipleTimeSeries<B> {
    private static final long serialVersionUID = -8768456438053526527L;

    @Getter
    private final String symbol;
    @Getter
    private final MainTimeSeries<B> mainTimeSeries;
    private final Map<BarPeriod, UniversalTimeSeries<B>> periodTimeSeriesMap;
    private final BarFactory<B> barFactory;
    private final UnaryOperator<UniversalTimeSeries<B>> wrapper;

    private BaseMultipleTimeSeries(String symbol, TimeSeriesDefinition timeSeriesDefinition, BarFactory<B> barFactory, UnaryOperator<UniversalTimeSeries<B>> wrapper) {
        this.symbol = symbol;
        this.barFactory = barFactory;
        this.wrapper = wrapper;
        this.mainTimeSeries = BaseMainTimeSeries.create(timeSeriesDefinition, symbol, barFactory);
        this.periodTimeSeriesMap = createPeriodTimeSeriesMap(timeSeriesDefinition.getBarPeriod());
    }

    public static <B extends Bar> BaseMultipleTimeSeries<B> create(String symbol, TimeSeriesDefinition timeSeriesDefinition, BarFactory<B> barFactory, UnaryOperator<UniversalTimeSeries<B>> wrapper) {
        return new BaseMultipleTimeSeries<>(symbol, timeSeriesDefinition, barFactory, wrapper);
    }

    public static <B extends Bar> BaseMultipleTimeSeries<B> create(String symbol, TimeSeriesDefinition timeSeriesDefinition, BarFactory<B> barFactory) {
        return new BaseMultipleTimeSeries<>(symbol, timeSeriesDefinition, barFactory, UnaryOperator.identity());
    }

    private Map<BarPeriod, UniversalTimeSeries<B>> createPeriodTimeSeriesMap(BarPeriod barPeriod) {
        final Map<BarPeriod, UniversalTimeSeries<B>> timeSeriesMap = new TreeMap<>(Comparator.comparing(BarPeriod::getPeriod)); // first period should save value first
        timeSeriesMap.put(barPeriod, wrap(mainTimeSeries));
        return timeSeriesMap;
    }

    @Override
    public MultipleTimeSeries<B> aggregate(TimeSeriesDefinition timeSeriesDefinition) {
        final AggregatedTimeSeries<B> timeSeries = mainTimeSeries.aggregate(timeSeriesDefinition);
        periodTimeSeriesMap.put(timeSeriesDefinition.getBarPeriod(), wrap(timeSeries));
        return this;
    }

    private UniversalTimeSeries<B> wrap(UniversalTimeSeries<B> timeSeries) {
        return wrapper.apply(timeSeries);
    }

    @Override
    public void updateBar(ZonedDateTime priceDate, double bid, double ask) {
        periodTimeSeriesMap.forEach((barPeriod, timeSeries) -> {
            if (empty(timeSeries) || isEqualOrAfter(priceDate, timeSeries.getLastBar().getEndTime())) {
                insertNewBar(priceDate, barPeriod, timeSeries);
            }
            final B lastBar = timeSeries.getLastBar();
            lastBar.addPrice(timeSeries.numOf(bid), timeSeries.numOf(ask));
        });
    }

    @Override
    public void updateBar(ZonedDateTime priceDate, double price) {
        periodTimeSeriesMap.forEach((barPeriod, timeSeries) -> {
            if (empty(timeSeries) || isEqualOrAfter(priceDate, timeSeries.getLastBar().getEndTime())) {
                insertNewBar(priceDate, barPeriod, timeSeries);
            }
            final Bar lastBar = timeSeries.getLastBar();
            lastBar.addPrice(timeSeries.numOf(price));
        });
    }

    @Override
    public void createBar(ZonedDateTime priceDate) {
        periodTimeSeriesMap.forEach((barPeriod, timeSeries) -> {
            if (empty(timeSeries)) {
                insertNewBar(priceDate, barPeriod, timeSeries);
            } else if (isEqualOrAfter(priceDate, timeSeries.getLastBar().getEndTime())) {
                insertNewBarWithLastPrice(priceDate, barPeriod, timeSeries);
            }
        });
    }

    private boolean empty(UniversalTimeSeries timeSeries) {
        return timeSeries.getBarCount() == 0;
    }

    private void insertNewBar(ZonedDateTime priceDate, BarPeriod barPeriod, UniversalTimeSeries<? super BidAskBar> timeSeries) {
        timeSeries.addBar(new BaseBidAskBar(barPeriod.getPeriod(),
                                            DateUtils.createEndDate(priceDate, barPeriod),
                                            timeSeries.function()));
    }

    private void insertNewBarWithLastPrice(ZonedDateTime priceDate, BarPeriod barPeriod, UniversalTimeSeries<? super BidAskBar> timeSeries) {
        final Num lastPrice = timeSeries.getLastBar().getClosePrice();
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
                                                     .getEndIndex())
                                  .orElse(-1);
    }

    @Override
    public UniversalTimeSeries<B> getTimeSeries(BarPeriod period) {
        return periodTimeSeriesMap.get(period);
    }

    @Override
    public List<UniversalTimeSeries<B>> getTimeSeries() {
        return periodTimeSeriesMap.entrySet().stream()
                                  .map(Map.Entry::getValue)
                                  .collect(Collectors.toList());
    }

}
