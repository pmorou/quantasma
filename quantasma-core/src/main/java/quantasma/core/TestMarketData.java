package quantasma.core;

import quantasma.core.timeseries.ManualIndexTimeSeries;
import quantasma.core.timeseries.MultipleTimeSeries;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class TestMarketData extends MarketData {

    public TestMarketData(Collection<? extends MultipleTimeSeries> multipleTimeSeries) {
        super(multipleTimeSeries);
    }

    public Set<ManualIndexTimeSeries> manualIndexTimeSeres() {
        return multipleTimeSeriesMap.entrySet().stream()
                                    .flatMap(entry -> entry.getValue().getTimeSeries().stream())
                                    .map(timeSeries -> (ManualIndexTimeSeries) timeSeries)
                                    .collect(Collectors.toSet());
    }
}
