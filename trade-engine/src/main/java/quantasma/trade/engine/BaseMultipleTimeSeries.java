package quantasma.trade.engine;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.num.Num;
import quantasma.model.CandlePeriod;
import quantasma.trade.engine.rule.TypedTimeSeries;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BaseMultipleTimeSeries implements MultipleTimeSeries {
    private static final long serialVersionUID = -8768456438053526527L;

    private final Map<CandlePeriod, TypedTimeSeries<BidAskBar>> timeSeriesMap;
    private final String instrument;

    private BaseMultipleTimeSeries(String instrument, Map<CandlePeriod, TypedTimeSeries<BidAskBar>> timeSeriesMap) {
        this.instrument = instrument;
        this.timeSeriesMap = timeSeriesMap;
    }

    public static List<MultipleTimeSeries> create(GroupTimeSeriesDefinition... groupTimeSeriesDefinition) {
        final List<MultipleTimeSeries> list = new LinkedList<>();

        for (GroupTimeSeriesDefinition timeSeriesDefinition : groupTimeSeriesDefinition) {
            for (String symbol : timeSeriesDefinition.getSymbols()) {
                Map<CandlePeriod, TypedTimeSeries<BidAskBar>> timeSeriesMap = new HashMap<>();
                for (TimeSeriesDefinition seriesDefinition : timeSeriesDefinition.getTimeSeriesDefinitions()) {
                    final TimeSeries timeSeries = new BaseTimeSeries.SeriesBuilder().withName(seriesDefinition.getCandlePeriod().getPeriodCode())
                                                                                    .withMaxBarCount(seriesDefinition.getPeriod())
                                                                                    .build();
                    timeSeriesMap.put(seriesDefinition.getCandlePeriod(), TypedTimeSeries.create(BidAskBar.class, timeSeries));
                }
                list.add(new BaseMultipleTimeSeries(symbol, timeSeriesMap));
            }

        }
        return list;
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
