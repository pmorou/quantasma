package quantasma.core.timeseries;

import org.ta4j.core.TimeSeries;

import java.util.function.Function;

public class AggregatedTimeSeriesFactory implements TimeSeriesFactory {

    private final TimeSeries timeSeries;

    private AggregatedTimeSeriesFactory(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
    }

    public static AggregatedTimeSeriesFactory from(TimeSeries timeSeries) {
        return new AggregatedTimeSeriesFactory(timeSeries);
    }

    @Override
    public Function<TimeSeriesDefinition, TimeSeries> function() {
        return timeSeriesDefinition -> new AggregatedTimeSeries(timeSeries, timeSeriesDefinition.getBarPeriod().getPeriodCode(), timeSeriesDefinition.getBarPeriod());
    }
}
