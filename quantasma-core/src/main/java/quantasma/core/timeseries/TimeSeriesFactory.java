package quantasma.core.timeseries;

import org.ta4j.core.TimeSeries;

import java.util.function.Function;

@FunctionalInterface
public interface TimeSeriesFactory<T extends TimeSeries> {

    Function<TimeSeriesDefinition, T> function();

    default T createInstance(TimeSeriesDefinition timeSeriesDefinition) {
        return function().apply(timeSeriesDefinition);
    }
}