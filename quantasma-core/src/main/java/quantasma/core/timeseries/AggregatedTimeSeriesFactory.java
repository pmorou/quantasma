package quantasma.core.timeseries;

import java.util.function.Function;

public class AggregatedTimeSeriesFactory implements TimeSeriesFactory<AggregatedTimeSeries> {

    private final MainTimeSeries mainTimeSeries;
    private final String symbol;

    private AggregatedTimeSeriesFactory(MainTimeSeries mainTimeSeries, String symbol) {
        this.mainTimeSeries = mainTimeSeries;
        this.symbol = symbol;
    }

    public static AggregatedTimeSeriesFactory from(MainTimeSeries timeSeries, String symbol) {
        return new AggregatedTimeSeriesFactory(timeSeries, symbol);
    }

    @Override
    public Function<TimeSeriesDefinition, AggregatedTimeSeries> function() {
        return timeSeriesDefinition -> new BaseAggregatedTimeSeries(mainTimeSeries,
                                                                    timeSeriesDefinition.getBarPeriod().getPeriodCode(),
                                                                    symbol,
                                                                    timeSeriesDefinition.getBarPeriod(),
                                                                    timeSeriesDefinition.getMaxBarCount());
    }
}
