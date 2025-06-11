package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.BarBuilder;
import org.ta4j.core.BarSeries;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;
import quantasma.core.timeseries.bar.UnmodifiableBar;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class UnmodifiableTimeSeries extends ForwardingTimeSeries {

    public UnmodifiableTimeSeries(BarSeries barSeries) {
        super(barSeries);
    }

    @Override
    public void addBar(Bar bar, boolean replace) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addTrade(Num tradeVolume, Num tradePrice) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPrice(Num price) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BarSeries getSubSeries(int startIndex, int endIndex) {
        return new UnmodifiableTimeSeries(delegate().getSubSeries(startIndex, endIndex));
    }

    @Override
    public List<Bar> getBarData() {
        return delegate().getBarData()
            .stream()
            .map(UnmodifiableBar::new)
            .collect(Collectors.collectingAndThen(Collectors.toList(),
                Collections::unmodifiableList));
    }

    @Override
    public NumFactory numFactory() {
        return delegate().numFactory();
    }

    @Override
    public BarBuilder barBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bar getBar(int i) {
        return new UnmodifiableBar(delegate().getBar(i));
    }

}
