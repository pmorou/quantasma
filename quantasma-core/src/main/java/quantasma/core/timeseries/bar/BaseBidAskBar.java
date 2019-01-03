package quantasma.core.timeseries.bar;

import org.ta4j.core.num.Num;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Function;

public class BaseBidAskBar extends BaseOneSidedBar implements BidAskBar {

    private static final long serialVersionUID = 8038383777467488148L;

    private Num askOpenPrice;
    private Num askMaxPrice;
    private Num askMinPrice;
    private Num askClosePrice;

    public BaseBidAskBar(Duration timePeriod, ZonedDateTime endTime, Function<Number, Num> numFunction) {
        super(timePeriod, endTime, numFunction);
    }

    public BaseBidAskBar(Duration timePeriod, ZonedDateTime endTime,
                         Num bidOpenPrice, Num bidHighPrice, Num bidLowPrice, Num bidClosePrice,
                         Num askOpenPrice, Num askMaxPrice, Num askMinPrice, Num askClosePrice,
                         Num volume, Num amount) {
        super(timePeriod, endTime, bidOpenPrice, bidHighPrice, bidLowPrice, bidClosePrice, volume, amount);
        this.askOpenPrice = askOpenPrice;
        this.askMaxPrice = askMaxPrice;
        this.askMinPrice = askMinPrice;
        this.askClosePrice = askClosePrice;
    }

    @Override
    public Num getAskOpenPrice() {
        return nonNull(askOpenPrice);
    }

    @Override
    public Num getAskMaxPrice() {
        return nonNull(askMaxPrice);
    }

    @Override
    public Num getAskMinPrice() {
        return nonNull(askMinPrice);
    }

    @Override
    public Num getAskClosePrice() {
        return nonNull(askClosePrice);
    }

    @Override
    public void addPrice(Num bid, Num ask) {
        addPrice(bid);

        if (askOpenPrice == null) {
            askOpenPrice = ask;
        }

        askClosePrice = ask;
        if (askMaxPrice == null) {
            askMaxPrice = ask;
        } else if (askMaxPrice.isLessThan(ask)) {
            askMaxPrice = ask;
        }
        if (askMinPrice == null) {
            askMinPrice = ask;
        } else if (askMinPrice.isGreaterThan(ask)) {
            askMinPrice = ask;
        }
    }

    @Override
    public String toString() {
        return String.format("{end time: %1s, bid close price: %2$f, bid open price: %3$f, bid min price: %4$f, bid max price: %5$f, "
                             + "ask close price: %6$f, ask open price: %7$f, ask min price: %8$f, ask max price: %9$f, volume: %10$f}",
                             getEndTime().withZoneSameInstant(ZoneId.systemDefault()),
                             getClosePrice().doubleValue(),
                             getOpenPrice().doubleValue(),
                             getMinPrice().doubleValue(),
                             getMaxPrice().doubleValue(),
                             getAskClosePrice().doubleValue(),
                             getAskOpenPrice().doubleValue(),
                             getAskMinPrice().doubleValue(),
                             getAskMaxPrice().doubleValue(),
                             getVolume().doubleValue());
    }

    public static class Builder<T> extends BarBuilder<T> {
        private Builder(BarBuilderContext<T> context) {
            super(context);
        }

        public BaseBidAskBar build(Duration timePeriod, ZonedDateTime endTime,
                                   T bidOpenPrice, T bidHighPrice, T bidLowPrice, T bidClosePrice,
                                   T askOpenPrice, T askHighPrice, T askLowPrice, T askClosePrice,
                                   T volume, T amount) {
            return new BaseBidAskBar(timePeriod, endTime,
                                     transform(bidOpenPrice), transform(bidHighPrice), transform(bidLowPrice), transform(bidClosePrice),
                                     transform(askOpenPrice), transform(askHighPrice), transform(askLowPrice), transform(askClosePrice),
                                     transform(volume), transform(amount));
        }

        public static <T> Builder<T> create(BarBuilderContext<T> context) {
            return new Builder<>(context);
        }
    }

    static class NaNBar extends BaseOneSidedBar.NaNBar implements BidAskBar {
        private static final NaNBar INSTANCE = new NaNBar();

        protected NaNBar() {
        }

        public static BidAskBar getInstance() {
            return INSTANCE;
        }

        @Override
        public Num getAskOpenPrice() {
            return NaN();
        }

        @Override
        public Num getAskMinPrice() {
            return NaN();
        }

        @Override
        public Num getAskMaxPrice() {
            return NaN();
        }

        @Override
        public Num getAskClosePrice() {
            return NaN();
        }

        @Override
        public void addPrice(Num bid, Num ask) {
            // do nothing
        }
    }
}
