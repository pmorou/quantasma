package quantasma.core;

import org.ta4j.core.Bar;
import quantasma.core.timeseries.MultipleTimeSeries;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MarketData<B extends Bar> {

    protected final Map<String, MultipleTimeSeries<B>> multipleTimeSeriesMap = new HashMap<>();

    public MarketData(Collection<? extends MultipleTimeSeries<B>> multipleTimeSeries) {
        for (MultipleTimeSeries<B> each : multipleTimeSeries) {
            this.multipleTimeSeriesMap.put(each.getSymbol(), each);
        }
    }

    public MultipleTimeSeries<B> of(String symbol) {
        final MultipleTimeSeries<B> multipleTimeSeries = multipleTimeSeriesMap.get(symbol);
        if (isUnknownSymbol(multipleTimeSeries)) {
            throw new IllegalArgumentException(String.format("[%s] is an unknown symbol", symbol));
        }
        return multipleTimeSeries;
    }

    public void add(String symbol, ZonedDateTime date, double price) {
        final MultipleTimeSeries<B> multipleTimeSeries = multipleTimeSeriesMap.get(symbol);
        if (isUnknownSymbol(multipleTimeSeries)) {
            return;
        }
        multipleTimeSeries.updateBar(date, price);
        ensureSameBarsNumberOverAllTimeSeries(symbol, date);
    }

    public void add(String symbol, ZonedDateTime date, double bid, double ask) {
        final MultipleTimeSeries<B> multipleTimeSeries = multipleTimeSeriesMap.get(symbol);
        if (isUnknownSymbol(multipleTimeSeries)) {
            return;
        }
        multipleTimeSeries.updateBar(date, bid, ask);
        ensureSameBarsNumberOverAllTimeSeries(symbol, date);
    }

    private static <B extends Bar> boolean isUnknownSymbol(MultipleTimeSeries<B> multipleTimeSeries) {
        return multipleTimeSeries == null;
    }

    private void ensureSameBarsNumberOverAllTimeSeries(String skipSymbol, ZonedDateTime dateToBeCoveredByBar) {
        multipleTimeSeriesMap.entrySet()
                             .stream()
                             .filter(s -> !s.getKey().equals(skipSymbol))
                             .map(Map.Entry::getValue)
                             .forEach(multipleTimeSeries -> multipleTimeSeries.createBar(dateToBeCoveredByBar));
    }

    public int lastBarIndex() {
        return multipleTimeSeriesMap.entrySet().stream().findFirst()
                                    .map(entry -> entry.getValue().lastBarIndex())
                                    .orElse(-1);
    }

}
