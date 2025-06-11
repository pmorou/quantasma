package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.BarBuilder;
import org.ta4j.core.BarSeries;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;

import java.util.List;

public abstract class ForwardingTimeSeries implements BarSeries {

    private final BarSeries barSeries;

    protected ForwardingTimeSeries(BarSeries timeSeries) {
        this.barSeries = timeSeries;
    }

    protected BarSeries delegate() {
        return barSeries;
    }

    @Override
    public String getName() {
        return barSeries.getName();
    }

    @Override
    public Bar getBar(int i) {
        return barSeries.getBar(i);
    }

    @Override
    public int getBarCount() {
        return barSeries.getBarCount();
    }

    @Override
    public List<Bar> getBarData() {
        return barSeries.getBarData();
    }

    @Override
    public int getBeginIndex() {
        return barSeries.getBeginIndex();
    }

    @Override
    public int getEndIndex() {
        return barSeries.getEndIndex();
    }

    @Override
    public void setMaximumBarCount(int maximumBarCount) {
        barSeries.setMaximumBarCount(maximumBarCount);
    }

    @Override
    public int getMaximumBarCount() {
        return barSeries.getMaximumBarCount();
    }

    @Override
    public int getRemovedBarsCount() {
        return barSeries.getRemovedBarsCount();
    }

    @Override
    public void addBar(Bar bar, boolean replace) {
        barSeries.addBar(bar, replace);
    }

    @Override
    public void addBar(Bar bar) {
        barSeries.addBar(bar);
    }

    @Override
    public void addTrade(Num tradeVolume, Num tradePrice) {
        barSeries.addTrade(tradeVolume, tradePrice);
    }

    @Override
    public void addPrice(Num price) {
        barSeries.addPrice(price);
    }

    @Override
    public BarSeries getSubSeries(int startIndex, int endIndex) {
        return barSeries.getSubSeries(startIndex, endIndex);
    }

    @Override
    public NumFactory numFactory() {
        return barSeries.numFactory();
    }

    @Override
    public BarBuilder barBuilder() {
        return barSeries.barBuilder();
    }
}
