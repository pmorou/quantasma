package quantasma.core.timeseries;

import org.ta4j.core.TimeSeries;
import quantasma.core.timeseries.AggregatedTimeSeries;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.core.timeseries.TimeSeriesFactory;

import java.util.function.Function;

public class AggregatedTimeSeriesFactory implements TimeSeriesFactory {

    private final TimeSeries timeSeries;

    public AggregatedTimeSeriesFactory(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
    }

    public static AggregatedTimeSeriesFactory timeSeriesSource(TimeSeries timeSeries) {
        return new AggregatedTimeSeriesFactory(timeSeries);
    }

    @Override
    public Function<TimeSeriesDefinition, TimeSeries> function() {
        return timeSeriesDefinition -> new AggregatedTimeSeries(timeSeries, timeSeriesDefinition.getCandlePeriod().getPeriodCode(), timeSeriesDefinition.getCandlePeriod());
    }
}
