package quantasma.core.timeseries;

import org.ta4j.core.TimeSeries;

public class BaseReferenceTimeSeries implements ReferenceTimeSeries {

    private static final TimeSeriesFactory REFERENCE_TIME_SERIES_FACTORY = new BaseTimeSeriesFactory();

    private final TimeSeries source;

    private BaseReferenceTimeSeries(TimeSeries source) {
        this.source = source;
    }

    public static ReferenceTimeSeries create(TimeSeriesDefinition timeSeriesDefinition) {
        return new BaseReferenceTimeSeries(REFERENCE_TIME_SERIES_FACTORY.createInstance(timeSeriesDefinition));
    }

    @Override
    public TimeSeries source() {
        return source;
    }

    @Override
    public TimeSeries aggregate(TimeSeriesDefinition timeSeriesDefinition) {
        return AggregatedTimeSeriesFactory.from(source).createInstance(timeSeriesDefinition);
    }
}
