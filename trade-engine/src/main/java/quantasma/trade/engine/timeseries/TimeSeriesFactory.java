package quantasma.trade.engine.timeseries;

import org.ta4j.core.TimeSeries;

import java.util.function.Function;

@FunctionalInterface
public interface TimeSeriesFactory {

    static TimeSeriesFactory create(Function<TimeSeriesDefinition, TimeSeries> timeSeriesFactory) {
        return () -> timeSeriesFactory;
    }

    Function<TimeSeriesDefinition, TimeSeries> function();

    default TimeSeries createInstance(TimeSeriesDefinition timeSeriesDefinition) {
        return function().apply(timeSeriesDefinition);
    }
}