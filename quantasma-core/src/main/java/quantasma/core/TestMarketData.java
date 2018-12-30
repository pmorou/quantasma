package quantasma.core;

import org.ta4j.core.Bar;
import quantasma.core.timeseries.ManualIndexTimeSeries;
import quantasma.core.timeseries.MultipleTimeSeries;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class TestMarketData<B extends Bar> extends MarketData<B> {

    public TestMarketData(Collection<? extends MultipleTimeSeries<B>> multipleTimeSeries) {
        super(multipleTimeSeries);
    }

    public Set<ManualIndexTimeSeries> manualIndexTimeSeres() {
        return multipleTimeSeriesMap.entrySet().stream()
                                    .flatMap(entry -> entry.getValue().getTimeSeries().stream())
                                    .map(timeSeries -> (ManualIndexTimeSeries) timeSeries)
                                    .collect(Collectors.toSet());
    }
}
