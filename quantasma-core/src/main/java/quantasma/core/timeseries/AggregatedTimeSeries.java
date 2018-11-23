package quantasma.core.timeseries;

import org.ta4j.core.TimeSeries;

public interface AggregatedTimeSeries extends TimeSeries {

    MainTimeSeries getMainTimeSeries();

}
