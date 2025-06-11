package quantasma.core.timeseries;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;
import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.BarFactory;
import quantasma.core.timeseries.bar.OneSidedBar;

import java.lang.reflect.Field;

public final class ReflectionManualIndexTimeSeries<B extends OneSidedBar> implements ManualIndexTimeSeries<B> {
    protected final GenericTimeSeries<B> timeSeries;

    private boolean isIndexModified;

    private ReflectionManualIndexTimeSeries(GenericTimeSeries<B> timeSeries) {
        this.timeSeries = timeSeries;
    }

    public static <B extends OneSidedBar> ReflectionManualIndexTimeSeries wrap(GenericTimeSeries<B> timeSeries) {
        return new ReflectionManualIndexTimeSeries<>(timeSeries);
    }

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
        return timeSeries.plainTimeSeries().getBarData().size();
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
        BarSeries target = timeSeries.plainTimeSeries();
        if (isForwardingTimeSeries(target)) {
            target = getDelegate(target);
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
        BarSeries target = timeSeries.plainTimeSeries();
        if (isForwardingTimeSeries(target)) {
            target = getDelegate(target);
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

    private boolean isForwardingTimeSeries(BarSeries target) {
        return target instanceof ForwardingTimeSeries;
    }

    private BarSeries getDelegate(BarSeries target) {
        return ((ForwardingTimeSeries) target).delegate();
    }

    private Class<?> getBaseTimeSeriesClassRef(BarSeries target) {
        Class<?> clazz = target.getClass();
        while (clazz != BaseBarSeries.class) {
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
    public BarSeries plainTimeSeries() {
        return timeSeries.plainTimeSeries();
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
    public NumFactory numFactory() {
        return timeSeries.numFactory();
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
