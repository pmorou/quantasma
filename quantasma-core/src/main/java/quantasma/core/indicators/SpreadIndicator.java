package quantasma.core.indicators;

import org.ta4j.core.Indicator;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.DifferenceIndicator;
import org.ta4j.core.num.Num;

public class SpreadIndicator extends CachedIndicator<Num> {

    private final DifferenceIndicator differenceIndicator;

    public SpreadIndicator(TimeSeries timeSeries, Indicator<Num> askIndicator, Indicator<Num> bidIndicator) {
        super(timeSeries);
        this.differenceIndicator = new DifferenceIndicator(askIndicator, bidIndicator);
    }

    @Override
    protected Num calculate(int index) {
        return differenceIndicator.getValue(index);
    }
}
