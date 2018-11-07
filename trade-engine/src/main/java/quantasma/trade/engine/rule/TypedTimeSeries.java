package quantasma.trade.engine.rule;

import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;

public class TypedTimeSeries<T extends Bar> {

    private final Class<T> barImplementation;
    private final TimeSeries timeSeries;

    private TypedTimeSeries(Class<T> barImplementation, TimeSeries timeSeries) {
        this.barImplementation = barImplementation;
        this.timeSeries = timeSeries;
    }

    public static <T extends Bar> TypedTimeSeries<T> create(Class<T> barImplementation, TimeSeries timeSeries) {
        return new TypedTimeSeries<>(barImplementation, timeSeries);
    }

    T getFirstBar() {
        return getBar(timeSeries.getBeginIndex());
    }

    T getLastBar() {
        return getBar(timeSeries.getEndIndex());
    }

    public void addBar(T bar) {
        timeSeries.addBar(bar);
    }

    public T getBar(int index) {
        return (T) timeSeries.getBar(index);
    }

    public TimeSeries getTimeSeries() {
        return timeSeries;
    }
}
