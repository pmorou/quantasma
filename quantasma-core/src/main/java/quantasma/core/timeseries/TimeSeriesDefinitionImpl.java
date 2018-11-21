package quantasma.core.timeseries;

import lombok.Data;
import quantasma.core.BarPeriod;

@Data
public class TimeSeriesDefinitionImpl implements TimeSeriesDefinition {

    private final BarPeriod barPeriod;
    private final int period;

    public TimeSeriesDefinitionImpl(BarPeriod barPeriod, int period) {
        this.barPeriod = barPeriod;
        this.period = period;
    }

}
