package quantasma.trade.engine;

import quantasma.trade.engine.timeseries.MultipleTimeSeries;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BaseDataService implements DataService {
    private final Map<String, MultipleTimeSeries> multipleTimeSeriesMap = new HashMap<>();

    public BaseDataService(Collection<? extends MultipleTimeSeries> multipleTimeSeries) {
        for (MultipleTimeSeries each : multipleTimeSeries) {
            this.multipleTimeSeriesMap.put(each.getInstrument(), each);
        }
    }

    public BaseDataService(MultipleTimeSeries... multipleTimeSeries) {
        for (MultipleTimeSeries each : multipleTimeSeries) {
            this.multipleTimeSeriesMap.put(each.getInstrument(), each);
        }
    }

    @Override
    public MultipleTimeSeries getMultipleTimeSeries(String symbol) {
        final MultipleTimeSeries multipleTimeSeries = multipleTimeSeriesMap.get(symbol);
        if (isUnknownSymbol(multipleTimeSeries)) {
            throw new IllegalArgumentException(String.format("[%s] is an unknown symbol", symbol));
        }
        return multipleTimeSeries;
    }

    @Override
    public void add(String symbol, ZonedDateTime date, double price) {
        final MultipleTimeSeries multipleTimeSeries = multipleTimeSeriesMap.get(symbol);
        if (isUnknownSymbol(multipleTimeSeries)) {
            return;
        }
        multipleTimeSeries.updateBar(date, price);
        ensureSameBarsNumberOverAllTimeSeries(symbol, date);
    }

    @Override
    public void add(String symbol, ZonedDateTime date, double bid, double ask) {
        final MultipleTimeSeries multipleTimeSeries = multipleTimeSeriesMap.get(symbol);
        if (isUnknownSymbol(multipleTimeSeries)) {
            return;
        }
        multipleTimeSeries.updateBar(date, bid, ask);
        ensureSameBarsNumberOverAllTimeSeries(symbol, date);
    }

    private static boolean isUnknownSymbol(MultipleTimeSeries multipleTimeSeries) {
        return multipleTimeSeries == null;
    }

    private void ensureSameBarsNumberOverAllTimeSeries(String skipSymbol, ZonedDateTime dateToBeCoveredByBar) {
        multipleTimeSeriesMap.entrySet()
                             .stream()
                             .filter(s -> !s.getKey().equals(skipSymbol))
                             .map(Map.Entry::getValue)
                             .forEach(multipleTimeSeries -> multipleTimeSeries.createBar(dateToBeCoveredByBar));
    }

    @Override
    public int lastBarIndex() {
        return multipleTimeSeriesMap.entrySet().stream().findFirst()
                                    .map(entry -> entry.getValue().lastBarIndex())
                                    .orElse(-1);
    }

}
