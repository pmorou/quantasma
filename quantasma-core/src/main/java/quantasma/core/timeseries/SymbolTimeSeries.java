package quantasma.core.timeseries;

import org.ta4j.core.TimeSeries;
import quantasma.core.BarPeriod;

public interface SymbolTimeSeries extends TimeSeries {

    BarPeriod getBarPeriod();

    String getSymbol();
}
