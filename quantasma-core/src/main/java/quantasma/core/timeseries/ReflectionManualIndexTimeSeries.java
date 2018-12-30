package quantasma.core.timeseries;

import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.num.Num;
import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.OneSideBar;
import quantasma.core.timeseries.bar.factory.BarFactory;

import java.lang.reflect.Field;
import java.util.function.Function;

public class ReflectionManualIndexTimeSeries<B extends OneSideBar> implements ManualIndexTimeSeries<B> {
    protected final UniversalTimeSeries<B> timeSeries;

    protected ReflectionManualIndexTimeSeries(UniversalTimeSeries<B> timeSeries) {
        this.timeSeries = timeSeries;
    }

    public static <B extends OneSideBar> ReflectionManualIndexTimeSeries wrap(UniversalTimeSeries<B> timeSeries) {
        return new ReflectionManualIndexTimeSeries<>(timeSeries);
    }

    protected boolean isIndexModified;

    @Override
    public void addBar(B bar, boolean replace) {
        if (isIndexModified) {
            throw new RuntimeException("Cannot add bars as indexes are already manipulated");
        }
        timeSeries.addBar(bar, replace);
    }

    @Override
    public void nextIndex() {
        if (stealEndIndex() == totalBarsCount() - 1) {
            throw new RuntimeException(String.format("No next bar available at index [%s] - bars count [%s]", stealBeginIndex() + 1, totalBarsCount()));
        }
        injectEndIndex(stealEndIndex() + 1);
    }

    @Override
    public void resetIndexes() {
        if (stealBeginIndex() < 0) {
            return;
        }

        setIndexModified();

        injectBeginIndex(0);
        injectEndIndex(-1);
    }

    protected void setIndexModified() {
        if (!isIndexModified) {
            isIndexModified = true;
        }
    }

    protected int totalBarsCount() {
        return getDelegate().getBarCount();
    }

    protected int stealBeginIndex() {
        return (int) getField("seriesBeginIndex");
    }

    protected void injectBeginIndex(int value) {
        setField("seriesBeginIndex", value);
    }

    protected int stealEndIndex() {
        return (int) getField("seriesEndIndex");
    }

    protected void injectEndIndex(int value) {
        setField("seriesEndIndex", value);
    }

    private Object getField(String fieldName) {
        TimeSeries target = timeSeries.timeSeries();
        if (isForwardingTimeSeries()) {
            target = getDelegate();
        }
        Class<?> clazz = getBaseTimeSeriesClassRef(target);

        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Checked exception", e);
        }
    }

    private void setField(String fieldName, Object value) {
        TimeSeries target = timeSeries.timeSeries();
        if (isForwardingTimeSeries()) {
            target = getDelegate();
        }
        Class<?> clazz = getBaseTimeSeriesClassRef(target);

        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Checked exception", e);
        }
    }

    private boolean isForwardingTimeSeries() {
        return timeSeries instanceof ForwardingTimeSeries;
    }

    private TimeSeries getDelegate() {
        return ((ForwardingTimeSeries) timeSeries).delegate();
    }

    private Class<?> getBaseTimeSeriesClassRef(TimeSeries target) {
        Class<?> clazz = target.getClass();
        while (clazz != BaseTimeSeries.class) {
            clazz = clazz.getSuperclass();
            if (clazz == Object.class) {
                throw new RuntimeException(String.format("Wrapped time series [%s] aren't related to BaseTimeSeries", target));
            }
        }
        return clazz;
    }

    // methods below do not modify delegate's logic

    @Override
    public String getName() {
        return timeSeries.getName();
    }

    @Override
    public TimeSeries timeSeries() {
        return timeSeries.timeSeries();
    }

    @Override
    public BarPeriod getBarPeriod() {
        return timeSeries.getBarPeriod();
    }

    @Override
    public String getSymbol() {
        return timeSeries.getSymbol();
    }

    @Override
    public BarFactory<B> getBarFactory() {
        return timeSeries.getBarFactory();
    }

    @Override
    public B getBar(int i) {
        return timeSeries.getBar(i);
    }

    @Override
    public int getBarCount() {
        return timeSeries.getBarCount();
    }

    @Override
    public int getBeginIndex() {
        return timeSeries.getBeginIndex();
    }

    @Override
    public int getEndIndex() {
        return timeSeries.getEndIndex();
    }

    @Override
    public void setMaximumBarCount(int maximumBarCount) {
        timeSeries.setMaximumBarCount(maximumBarCount);
    }

    @Override
    public int getMaximumBarCount() {
        return timeSeries.getMaximumBarCount();
    }

    @Override
    public int getRemovedBarsCount() {
        return timeSeries.getRemovedBarsCount();
    }

    @Override
    public void addBar() {
        timeSeries.addBar();
    }

    @Override
    public void addTrade(Num tradeVolume, Num tradePrice) {
        timeSeries.addTrade(tradeVolume, tradePrice);
    }

    @Override
    public void addPrice(Num price) {
        timeSeries.addPrice(price);
    }

    @Override
    public Num numOf(Number number) {
        return timeSeries.numOf(number);
    }

    @Override
    public Function<Number, Num> function() {
        return timeSeries.function();
    }

    @Override
    public B getFirstBar() {
        return timeSeries.getFirstBar();
    }

    @Override
    public B getLastBar() {
        return timeSeries.getLastBar();
    }

}
