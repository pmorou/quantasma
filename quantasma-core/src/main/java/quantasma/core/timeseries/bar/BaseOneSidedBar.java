package quantasma.core.timeseries.bar;

import org.ta4j.core.BaseBar;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;
import quantasma.core.timeseries.bar.generic.Argument;
import quantasma.core.timeseries.bar.generic.GenericNumMethod;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.function.Function;

public class BaseOneSidedBar extends ForwardingBar implements OneSidedBar {

    private final NumFactory numFactory;

    public BaseOneSidedBar(Duration timePeriod, Instant endTime, NumFactory numFactory) {
        super(new BaseBar(timePeriod, endTime, null, null, null, null, null, null, 0));
        this.numFactory = numFactory;
    }

    public BaseOneSidedBar(Duration timePeriod, Instant endTime,
                           Num openPrice, Num highPrice, Num lowPrice, Num closePrice,
                           Num volume, Num amount) {
        super(new BaseBar(timePeriod, endTime, openPrice, highPrice, lowPrice, closePrice, volume, amount, 0));
        this.numFactory = openPrice.getNumFactory();
    }

    @Override
    public Num getOpenPrice() {
        return nonNull(super.getOpenPrice());
    }

    @Override
    public Num getLowPrice() {
        return nonNull(super.getLowPrice());
    }

    @Override
    public Num getHighPrice() {
        return nonNull(super.getHighPrice());
    }

    @Override
    public Num getClosePrice() {
        return nonNull(super.getClosePrice());
    }

    @Override
    public Num getVolume() {
        return nonNull(super.getVolume());
    }

    @Override
    public Num getAmount() {
        return nonNull(super.getAmount());
    }

    /**
     * Bar only helper method
     *
     * @param num
     * @return Non-null Num
     */
    protected final Num nonNull(Num num) {
        return num == null ? NaN.NaN : num;
    }

    public NumFactory numFactory() {
        return numFactory;
    }

    @Override
    public String toString() {
        return String.format("{end time: %1s, close price: %2$f, open price: %3$f, min price: %4$f, max price: %5$f, volume: %6$f}",
                             getEndTime().atZone(ZoneId.systemDefault()),
                             getClosePrice().doubleValue(),
                             getOpenPrice().doubleValue(),
                             getLowPrice().doubleValue(),
                             getHighPrice().doubleValue(),
                             getVolume().doubleValue());
    }

    public static class GenericConstructor<T> extends GenericNumMethod<T> {
        protected GenericConstructor(Function<Number, Num> numFunction, Argument<T> context) {
            super(numFunction, context);
        }

        public BaseOneSidedBar create(Duration timePeriod, Instant endTime,
                                      T openPrice, T highPrice, T lowPrice, T closePrice,
                                      T volume, T amount) {
            return new BaseOneSidedBar(timePeriod, endTime,
                                       transform(openPrice), transform(highPrice), transform(lowPrice), transform(closePrice),
                                       transform(volume), transform(amount));
        }

        public static <T> GenericConstructor<T> from(Function<Number, Num> numFunction, Argument<T> context) {
            return new GenericConstructor<>(numFunction, context);
        }
    }

    static class NaNBar implements OneSidedBar {
        private static final NaNBar INSTANCE = new NaNBar();

        protected final Num NaN() {
            return org.ta4j.core.num.NaN.NaN;
        }

        protected NaNBar() {
        }

        public static OneSidedBar getInstance() {
            return INSTANCE;
        }

        public NumFactory numFactory() {
            return NaN().getNumFactory();
        }

        @Override
        public Num getOpenPrice() {
            return NaN();
        }

        @Override
        public Num getLowPrice() {
            return NaN();
        }

        @Override
        public Num getHighPrice() {
            return NaN();
        }

        @Override
        public Num getClosePrice() {
            return NaN();
        }

        @Override
        public Num getVolume() {
            return NaN();
        }

        @Override
        public long getTrades() {
            return 0;
        }

        @Override
        public Num getAmount() {
            return NaN();
        }

        @Override
        public Duration getTimePeriod() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Instant getBeginTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Instant getEndTime() {
            throw new UnsupportedOperationException();
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
}
