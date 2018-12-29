package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.num.Num;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;

abstract public class ForwardingTimeSeries implements TimeSeries {

    private final TimeSeries timeSeries;

    protected ForwardingTimeSeries(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
    }

    protected TimeSeries delegate() {
        return timeSeries;
    }

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
    public void addBar(Bar bar, boolean replace) {
        timeSeries.addBar(bar, replace);
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
}
