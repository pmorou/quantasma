package quantasma.core;

import quantasma.core.timeseries.MultipleTimeSeries;
import quantasma.core.timeseries.UniversalTimeSeries;
import quantasma.core.timeseries.bar.OneSidedBar;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarketData<B extends OneSidedBar> {
    protected final Map<String, MultipleTimeSeries<B>> multipleTimeSeriesMap = new HashMap<>();

    public MarketData(Collection<? extends MultipleTimeSeries<B>> multipleTimeSeries) {
        for (MultipleTimeSeries<B> each : multipleTimeSeries) {
            this.multipleTimeSeriesMap.put(each.getSymbol(), each);
        }
    }

    public List<UniversalTimeSeries<B>> allTimeSeries() {
        return multipleTimeSeriesMap.entrySet().stream()
                                    .flatMap(entry -> entry.getValue().getTimeSeries().stream())
                                    .collect(Collectors.toList());
    }

    public MultipleTimeSeries<B> of(String symbol) {
        final MultipleTimeSeries<B> multipleTimeSeries = multipleTimeSeriesMap.get(symbol);
        if (isUnknownSymbol(multipleTimeSeries)) {
            throw new IllegalArgumentException(String.format("[%s] is an unknown symbol", symbol));
        }
        return multipleTimeSeries;
    }

    protected void ensureSameBarsNumberOverAllTimeSeries(String skipSymbol, ZonedDateTime dateToBeCoveredByBar) {
        multipleTimeSeriesMap.entrySet()
                             .stream()
                             .filter(s -> !s.getKey().equals(skipSymbol))
                             .map(Map.Entry::getValue)
                             .forEach(multipleTimeSeries -> multipleTimeSeries.createBar(dateToBeCoveredByBar));
    }

    protected boolean isUnknownSymbol(MultipleTimeSeries<?> multipleTimeSeries) {
        return multipleTimeSeries == null;
    }

    public int lastBarIndex() {
        return multipleTimeSeriesMap.entrySet().stream().findFirst()
                                    .map(entry -> entry.getValue().lastBarIndex())
                                    .orElse(-1);
    }

    public void add(Quote quote) {
        final MultipleTimeSeries<B> multipleTimeSeries = multipleTimeSeriesMap.get(quote.getSymbol());
        if (isUnknownSymbol(multipleTimeSeries)) {
            return;
        }
        multipleTimeSeries.updateBar(quote);
        ensureSameBarsNumberOverAllTimeSeries(quote.getSymbol(), quote.getTime());
    }

}
