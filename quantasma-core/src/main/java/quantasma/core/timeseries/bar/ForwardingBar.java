package quantasma.core.timeseries.bar;

import org.ta4j.core.Bar;
import org.ta4j.core.num.Num;

import java.time.Duration;
import java.time.Instant;

public abstract class ForwardingBar implements Bar {

    private final Bar bar;

    ForwardingBar(Bar bar) {
        this.bar = bar;
    }

    @Override
    public Num getOpenPrice() {
        return bar.getOpenPrice();
    }

    @Override
    public Num getLowPrice() {
        return bar.getLowPrice();
    }

    @Override
    public Num getHighPrice() {
        return bar.getHighPrice();
    }

    @Override
    public Num getClosePrice() {
        return bar.getClosePrice();
    }

    @Override
    public Num getVolume() {
        return bar.getVolume();
    }

    @Override
    public long getTrades() {
        return bar.getTrades();
    }

    @Override
    public Num getAmount() {
        return bar.getAmount();
    }

    @Override
    public Duration getTimePeriod() {
        return bar.getTimePeriod();
    }

    @Override
    public Instant getBeginTime() {
        return bar.getBeginTime();
    }

    @Override
    public Instant getEndTime() {
        return bar.getEndTime();
    }

    @Override
    public void addTrade(Num tradeVolume, Num tradePrice) {
        bar.addTrade(tradeVolume, tradePrice);
    }

    @Override
    public void addPrice(Num price) {
        bar.addPrice(price);
    }
}
