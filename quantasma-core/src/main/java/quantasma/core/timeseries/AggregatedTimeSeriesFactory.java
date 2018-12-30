package quantasma.core.timeseries;

import org.ta4j.core.Bar;

import java.util.function.Function;

public class AggregatedTimeSeriesFactory<B extends Bar> implements TimeSeriesFactory<AggregatedTimeSeries<B>> {

    private final MainTimeSeries mainTimeSeries;

    private AggregatedTimeSeriesFactory(MainTimeSeries mainTimeSeries) {
        this.mainTimeSeries = mainTimeSeries;
    }

    public static <B extends Bar> AggregatedTimeSeriesFactory<B> from(MainTimeSeries<B> timeSeries) {
        return new AggregatedTimeSeriesFactory<>(timeSeries);
    }

    @Override
    public Function<TimeSeriesDefinition, AggregatedTimeSeries<B>> function() {
        return timeSeriesDefinition -> new BaseAggregatedTimeSeries.Builder<>(mainTimeSeries.getSymbol(),
                                                                              timeSeriesDefinition.getBarPeriod(),
                                                                              mainTimeSeries,
                                                                              mainTimeSeries.getBarFactory())
                .withMaxBarCount(timeSeriesDefinition.getMaxBarCount())
                .withName(timeSeriesDefinition.getBarPeriod().getPeriodCode())
                .build();
    }
}
