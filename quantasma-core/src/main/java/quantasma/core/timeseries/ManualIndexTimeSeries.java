package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.num.Num;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;

public class ManualIndexTimeSeries implements TimeSeries {
    private final TimeSeries timeSeries;

    public ManualIndexTimeSeries(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
    }

    private boolean isIndexModified;

    @Override
    public void addBar(Bar bar, boolean replace) {
        if (isIndexModified) {
            throw new RuntimeException("Cannot add bars as indexes are already manipulated");
        }
        timeSeries.addBar(bar, replace);
    }

    public void resetIndexes() {
        if (stealBeginIndex() < 0) {
            return;
        }

        ensureIndexesManipulated();

        injectBeginIndex(0);
        injectEndIndex(-1);
    }

    private void ensureIndexesManipulated() {
        if (!isIndexModified) {
            isIndexModified = true;
        }
    }

    public void nextIndex() {
        if (stealEndIndex() == totalBarsCount() - 1) {
            throw new RuntimeException(String.format("No next bar available at index [%s] - bars count [%s]", stealBeginIndex() + 1, totalBarsCount()));
        }
        injectEndIndex(stealEndIndex() + 1);
    }

    private int totalBarsCount() {
        return getBarData().size();
    }

    private int stealBeginIndex() {
        return (int) getField("seriesBeginIndex");
    }

    private void injectBeginIndex(int value) {
        setField("seriesBeginIndex", value);
    }

    private int stealEndIndex() {
        return (int) getField("seriesEndIndex");
    }

    private void injectEndIndex(int value) {
        setField("seriesEndIndex", value);
    }

    private Object getField(String fieldName) {
        try {
            final Field seriesEndIndex = timeSeries.getClass().getDeclaredField(fieldName);
            seriesEndIndex.setAccessible(true);
            return seriesEndIndex.get(timeSeries);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Caught checked exception", e);
        }
    }

    private void setField(String fieldName, Object value) {
        try {
            final Field seriesEndIndex = timeSeries.getClass().getDeclaredField(fieldName);
            seriesEndIndex.setAccessible(true);
            seriesEndIndex.set(timeSeries, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Caught checked exception", e);
        }
    }

    // methods below do not modify delegate's logic

    @Override
    public String getName() {
        return timeSeries.getName();
    }

    @Override
    public Bar getBar(int i) {
        return timeSeries.getBar(i);
    }

    @Override
    public int getBarCount() {
        return timeSeries.getBarCount();
    }

    @Override
    public List<Bar> getBarData() {
        return timeSeries.getBarData();
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
    public void addBar(Duration timePeriod, ZonedDateTime endTime) {
        timeSeries.addBar(timePeriod, endTime);
    }

    @Override
    public void addBar(ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice, Num volume, Num amount) {
        timeSeries.addBar(endTime, openPrice, highPrice, lowPrice, closePrice, volume, amount);
    }

    @Override
    public void addBar(Duration timePeriod, ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice, Num volume) {
        timeSeries.addBar(timePeriod, endTime, openPrice, highPrice, lowPrice, closePrice, volume);
    }

    @Override
    public void addBar(Duration timePeriod, ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice, Num volume, Num amount) {
        timeSeries.addBar(timePeriod, endTime, openPrice, highPrice, lowPrice, closePrice, volume, amount);
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
    public TimeSeries getSubSeries(int startIndex, int endIndex) {
        return timeSeries.getSubSeries(startIndex, endIndex);
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
    public Bar getFirstBar() {
        return timeSeries.getFirstBar();
    }

    @Override
    public Bar getLastBar() {
        return timeSeries.getLastBar();
    }

}
