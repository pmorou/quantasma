package quantasma.trade.engine;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.num.Num;
import quantasma.model.CandlePeriod;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BaseMultipleTimeSeries implements MultipleTimeSeries {
    private static final long serialVersionUID = -8768456438053526527L;

    private final Map<CandlePeriod, TimeSeries> timeSeriesMap;
    private final String instrument;

    private BaseMultipleTimeSeries(String instrument, Map<CandlePeriod, TimeSeries> timeSeriesMap) {
        this.instrument = instrument;
        this.timeSeriesMap = timeSeriesMap;
    }

    public static List<MultipleTimeSeries> create(GroupTimeSeriesDefinition... groupTimeSeriesDefinition) {
        final List<MultipleTimeSeries> list = new LinkedList<>();

        for (GroupTimeSeriesDefinition timeSeriesDefinition : groupTimeSeriesDefinition) {
            for (String symbol : timeSeriesDefinition.getSymbols()) {
                Map<CandlePeriod, TimeSeries> timeSeriesMap = new HashMap<>();
                for (TimeSeriesDefinition seriesDefinition : timeSeriesDefinition.getTimeSeriesDefinitions()) {
                    final TimeSeries timeSeries = new BaseTimeSeries.SeriesBuilder().withName(seriesDefinition.getCandlePeriod().getPeriodCode())
                                                                                    .withMaxBarCount(seriesDefinition.getPeriod())
                                                                                    .build();
                    timeSeriesMap.put(seriesDefinition.getCandlePeriod(), timeSeries);
                }
                list.add(new BaseMultipleTimeSeries(symbol, timeSeriesMap));
            }

        }
        return list;
    }

    @Override
    public void updateBar(ZonedDateTime priceDate, double bid, double ask) {
        timeSeriesMap.forEach((candlePeriod, timeSeries) -> {
            if (empty(timeSeries) || isEqualOrAfter(priceDate, timeSeries.getLastBar().getEndTime())) {
                insertNewBar(priceDate, candlePeriod, timeSeries);
            }
            final BidAskBar lastBar = (BidAskBar) timeSeries.getLastBar();
            lastBar.addPrice(timeSeries.numOf(bid), timeSeries.numOf(ask));
        });
    }

    @Override
    public void updateBar(ZonedDateTime priceDate, double price) {
        timeSeriesMap.forEach((candlePeriod, timeSeries) -> {
            if (empty(timeSeries) || isEqualOrAfter(priceDate, timeSeries.getLastBar().getEndTime())) {
                insertNewBar(priceDate, candlePeriod, timeSeries);
            }
            final Bar lastBar = timeSeries.getLastBar();
            lastBar.addPrice(timeSeries.numOf(price));
        });
    }

    @Override
    public void createBar(ZonedDateTime priceDate) {
        timeSeriesMap.forEach((candlePeriod, timeSeries) -> {
            if (empty(timeSeries)) {
                insertNewBar(priceDate, candlePeriod, timeSeries);
            } else if (isEqualOrAfter(priceDate, timeSeries.getLastBar().getEndTime())) {
                insertNewBarWithLastPrice(priceDate, candlePeriod, timeSeries);
            }
        });
    }

    private boolean empty(TimeSeries timeSeries) {
        return timeSeries.getBarCount() == 0;
    }

    private void insertNewBar(ZonedDateTime priceDate, CandlePeriod candlePeriod, TimeSeries timeSeries) {
        timeSeries.addBar(new BaseBidAskBar(candlePeriod.getPeriod(),
                                            DateUtils.createEndDate(priceDate, candlePeriod),
                                            timeSeries.function()));
    }

    private void insertNewBarWithLastPrice(ZonedDateTime priceDate, CandlePeriod candlePeriod, TimeSeries timeSeries) {
        final Num lastPrice = timeSeries.getLastBar().getClosePrice();
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
                            .map(Map.Entry::getValue)
                            .map(TimeSeries::getEndIndex)
                            .orElse(-1);
    }

    @Override
    public String getInstrument() {
        return instrument;
    }

    @Override
    public TimeSeries getTimeSeries(CandlePeriod period) {
        return timeSeriesMap.get(period);
    }
}
