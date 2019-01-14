package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.num.Num;
import quantasma.core.timeseries.bar.UnmodifiableBar;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class UnmodifiableTimeSeries extends ForwardingTimeSeries {

    public UnmodifiableTimeSeries(TimeSeries timeSeries) {
        super(timeSeries);
    }

    @Override
    public void addBar(Bar bar, boolean replace) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addBar(Duration timePeriod, ZonedDateTime endTime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addBar(ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice, Num volume, Num amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addBar(Duration timePeriod, ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice, Num volume) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addBar(Duration timePeriod, ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice, Num volume, Num amount) {
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
    public TimeSeries getSubSeries(int startIndex, int endIndex) {
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
    public Bar getBar(int i) {
        return new UnmodifiableBar(delegate().getBar(i));
    }

}
