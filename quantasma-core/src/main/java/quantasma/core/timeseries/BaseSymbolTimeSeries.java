package quantasma.core.timeseries;

import lombok.Getter;
import org.ta4j.core.BaseTimeSeries;
import quantasma.core.BarPeriod;

@Getter
public class BaseSymbolTimeSeries extends BaseTimeSeries implements SymbolTimeSeries {
    private final String symbol;
    private final BarPeriod barPeriod;

    protected BaseSymbolTimeSeries(String name, String symbol, BarPeriod barPeriod, int maxBarCount) {
        super(name);
        this.barPeriod = barPeriod;
        this.symbol = symbol;
        setMaximumBarCount(maxBarCount);
    }

    protected BaseSymbolTimeSeries(String name, String symbol, BarPeriod barPeriod) {
        this(name, symbol, barPeriod, Integer.MAX_VALUE);
    }

}
