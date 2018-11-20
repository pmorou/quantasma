package quantasma.trade.engine;

import org.ta4j.core.Order;
import org.ta4j.core.num.Num;

import java.time.Duration;
import java.time.ZonedDateTime;

public class NaNBar implements BidAskBar {

    public static final BidAskBar NaN = new NaNBar();

    private static final Num Num_NaN = org.ta4j.core.num.NaN.NaN;

    private NaNBar() {
    }

    @Override
    public Num getBidOpenPrice() {
        return Num_NaN;
    }

    @Override
    public Num getBidMinPrice() {
        return Num_NaN;
    }

    @Override
    public Num getBidMaxPrice() {
        return Num_NaN;
    }

    @Override
    public Num getBidClosePrice() {
        return Num_NaN;
    }

    @Override
    public Num getAskOpenPrice() {
        return Num_NaN;
    }

    @Override
    public Num getAskMinPrice() {
        return Num_NaN;
    }

    @Override
    public Num getAskMaxPrice() {
        return Num_NaN;
    }

    @Override
    public Num getAskClosePrice() {
        return Num_NaN;
    }

    @Override
    public void addTrade(Num volume, Num bid, Num ask, Order.OrderType orderType) {
        // do nothing
    }

    @Override
    public void addPrice(Num bid, Num ask) {
        // do nothing
    }

    @Override
    public Num getVolume() {
        return Num_NaN;
    }

    @Override
    public int getTrades() {
        return 0;
    }

    @Override
    public Num getAmount() {
        return Num_NaN;
    }

    @Override
    public Duration getTimePeriod() {
        return Duration.ZERO;
    }

    @Override
    public ZonedDateTime getBeginTime() {
        return null;
    }

    @Override
    public ZonedDateTime getEndTime() {
        return null;
    }

    @Override
    public void addTrade(Num tradeVolume, Num tradePrice) {
        // do nothing
    }

    @Override
    public void addPrice(Num price) {
        // do nothing
    }
}
