package quantasma.core.timeseries;

import java.util.function.Function;

public class AggregatedTimeSeriesFactory implements TimeSeriesFactory<AggregatedTimeSeries> {

    private final MainTimeSeries mainTimeSeries;

    private AggregatedTimeSeriesFactory(MainTimeSeries mainTimeSeries) {
        this.mainTimeSeries = mainTimeSeries;
    }

    public static AggregatedTimeSeriesFactory from(MainTimeSeries timeSeries) {
        return new AggregatedTimeSeriesFactory(timeSeries);
    }

    @Override
    public Function<TimeSeriesDefinition, AggregatedTimeSeries> function() {
        return timeSeriesDefinition -> new BaseAggregatedTimeSeries.Builder<>(mainTimeSeries.getSymbol(), timeSeriesDefinition.getBarPeriod(), mainTimeSeries)
                .withMaxBarCount(timeSeriesDefinition.getMaxBarCount())
                .withName(timeSeriesDefinition.getBarPeriod().getPeriodCode())
                .build();
    }
}
