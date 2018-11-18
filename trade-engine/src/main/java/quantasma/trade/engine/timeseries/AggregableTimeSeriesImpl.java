package quantasma.trade.engine.timeseries;

import org.ta4j.core.TimeSeries;

public class AggregableTimeSeriesImpl implements AggregableTimeSeries {

    private final TimeSeries baseTimeSeries;

    public AggregableTimeSeriesImpl(TimeSeries baseTimeSeries) {
        this.baseTimeSeries = baseTimeSeries;
    }

    @Override
    public TimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition) {
        return new AggregatedTimeSeries(baseTimeSeries, timeSeriesDefinition.getCandlePeriod().getPeriodCode(), timeSeriesDefinition.getCandlePeriod());
    }
}
