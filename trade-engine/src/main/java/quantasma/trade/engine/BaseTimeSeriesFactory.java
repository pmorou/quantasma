package quantasma.trade.engine;

import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;

import java.util.function.Function;

public class BaseTimeSeriesFactory implements TimeSeriesFactory {
    @Override
    public Function<TimeSeriesDefinition, TimeSeries> function() {
        return timeSeriesDefinition -> new BaseTimeSeries.SeriesBuilder().withName(timeSeriesDefinition.getCandlePeriod().getPeriodCode())
                                                                         .withMaxBarCount(timeSeriesDefinition.getPeriod())
                                                                         .build();
    }
}
