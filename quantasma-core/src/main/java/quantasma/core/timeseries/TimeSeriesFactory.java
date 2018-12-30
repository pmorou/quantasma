package quantasma.core.timeseries;

import java.util.function.Function;

@FunctionalInterface
public interface TimeSeriesFactory<T extends UniversalTimeSeries> {

    Function<TimeSeriesDefinition, T> function();

    default T createInstance(TimeSeriesDefinition timeSeriesDefinition) {
        return function().apply(timeSeriesDefinition);
    }
}