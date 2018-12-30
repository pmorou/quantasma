package quantasma.core.timeseries;

import org.ta4j.core.Bar;

public interface AggregatedTimeSeries<B extends Bar> extends UniversalTimeSeries<B> {

    MainTimeSeries getMainTimeSeries();

}
