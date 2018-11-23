package quantasma.core.timeseries;

import org.ta4j.core.BaseTimeSeries;
import quantasma.core.BarPeriod;

public class BaseDescribedTimeSeries extends BaseTimeSeries implements DescribedTimeSeries {
    private final BarPeriod barPeriod;
    private final String symbol;

    protected BaseDescribedTimeSeries(String name, String symbol, BarPeriod barPeriod, int maxBarCount) {
        super(name);
        this.barPeriod = barPeriod;
        this.symbol = symbol;
        setMaximumBarCount(maxBarCount);
    }

    protected BaseDescribedTimeSeries(String name, String symbol, BarPeriod barPeriod) {
        this(name, symbol, barPeriod, Integer.MAX_VALUE);
    }

    @Override
    public BarPeriod getBarPeriod() {
        return barPeriod;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }
}
