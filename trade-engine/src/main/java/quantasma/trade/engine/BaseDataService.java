package quantasma.trade.engine;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BaseDataService implements DataService {
    private final Map<String, MultipleTimeSeries> multipleTimeSeriesMap = new HashMap<>();

    public BaseDataService(Collection<MultipleTimeSeries> multipleTimeSeries) {
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
        return multipleTimeSeriesMap.get(symbol);
    }

    @Override
    public void add(String symbol, ZonedDateTime date, double price) {
        multipleTimeSeriesMap.get(symbol).updateBar(date, price);
        ensureSameBarsNumberOverAllTimeSeries(symbol, date);
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
