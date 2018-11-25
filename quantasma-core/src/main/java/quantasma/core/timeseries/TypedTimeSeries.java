package quantasma.core.timeseries;

import lombok.Getter;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;

public class TypedTimeSeries<T extends Bar> {

    private final Class<T> barImplementation;
    @Getter
    private final TimeSeries timeSeries;

    private TypedTimeSeries(Class<T> barImplementation, TimeSeries timeSeries) {
        this.barImplementation = barImplementation;
        this.timeSeries = timeSeries;
    }

    public static <T extends Bar> TypedTimeSeries<T> create(Class<T> barImplementation, TimeSeries timeSeries) {
        return new TypedTimeSeries<>(barImplementation, timeSeries);
    }

    public T getFirstBar() {
        return (T) timeSeries.getFirstBar();
    }

    public T getLastBar() {
        return (T) timeSeries.getLastBar();
    }

    public void addBar(T bar) {
        timeSeries.addBar(bar);
    }

    public T getBar(int index) {
        return (T) timeSeries.getBar(index);
    }

}
