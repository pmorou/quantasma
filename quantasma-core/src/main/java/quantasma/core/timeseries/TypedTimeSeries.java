package quantasma.core.timeseries;

import lombok.Getter;
import org.ta4j.core.TimeSeries;
import quantasma.core.timeseries.bar.OneSideBar;

public class TypedTimeSeries<T extends OneSideBar> {

    private final Class<T> barImplementation;
    @Getter
    private final TimeSeries timeSeries;

    private TypedTimeSeries(Class<T> barImplementation, TimeSeries timeSeries) {
        this.barImplementation = barImplementation;
        this.timeSeries = timeSeries;
    }

    public static <T extends OneSideBar> TypedTimeSeries<T> create(Class<T> barImplementation, TimeSeries timeSeries) {
        return new TypedTimeSeries<>(barImplementation, timeSeries);
    }

    @SuppressWarnings("unchecked")
    public T getFirstBar() {
        return (T) timeSeries.getFirstBar();
    }

    @SuppressWarnings("unchecked")
    public T getLastBar() {
        return (T) timeSeries.getLastBar();
    }

    public void addBar(T bar) {
        timeSeries.addBar(bar);
    }

    @SuppressWarnings("unchecked")
    public T getBar(int index) {
        return (T) timeSeries.getBar(index);
    }

}
