package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;
import quantasma.core.BarPeriod;

public interface UniversalTimeSeries<B extends Bar> extends TimeSeries {

    BarPeriod getBarPeriod();

    String getSymbol();

    @Override
    B getBar(int i);

    @Override
    default B getFirstBar() {
        return getBar(getBeginIndex());
    }

    @Override
    default B getLastBar() {
        return getBar(getEndIndex());
    }
}
