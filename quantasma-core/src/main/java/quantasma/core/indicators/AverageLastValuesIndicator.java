package quantasma.core.indicators;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

import java.util.stream.IntStream;

public class AverageLastValuesIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> indicator;
    private final int lastValues;

    public AverageLastValuesIndicator(Indicator<Num> indicator, int lastValues) {
        super(indicator);
        if (lastValues <= 0) {
            throw new IllegalArgumentException(String.format("Provided value [%s] should be greater than 0", lastValues));
        }
        this.indicator = indicator;
        this.lastValues = lastValues;
    }

    @Override
    protected Num calculate(int index) {
        if (getTimeSeries().getBarCount() < lastValues) {
            return NaN.NaN;
        }

        return IntStream.iterate(index, operand -> --operand)
            .mapToObj(indicator::getValue)
            .limit(lastValues)
            .reduce(getTimeSeries().numOf(0), Num::plus)
            .dividedBy(getTimeSeries().numOf(lastValues));
    }
}
