package quantasma.core.timeseries.bar;

import org.ta4j.core.BaseBar;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.function.Function;

public class BaseOneSidedBar extends ForwardingBar implements OneSidedBar {

    private final Function<Number, Num> numFunction;

    public BaseOneSidedBar(Duration timePeriod, ZonedDateTime endTime, Function<Number, Num> numFunction) {
        super(new BaseBar(timePeriod, endTime, null, null, null, null, null, null));
        this.numFunction = numFunction;
    }

    protected BaseOneSidedBar(Duration timePeriod, ZonedDateTime endTime,
                              Num openPrice, Num highPrice, Num lowPrice, Num closePrice,
                              Num volume, Num amount) {
        super(new BaseBar(timePeriod, endTime, openPrice, highPrice, lowPrice, closePrice, volume, amount));
        this.numFunction = openPrice.function();
    }

    @Override
    public Num getOpenPrice() {
        return nonNull(super.getOpenPrice());
    }

    @Override
    public Num getMinPrice() {
        return nonNull(super.getMinPrice());
    }

    @Override
    public Num getMaxPrice() {
        return nonNull(super.getMaxPrice());
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
    final protected Num nonNull(Num num) {
        return num == null ? NaN.NaN : num;
    }

    @Override
    public Function<Number, Num> function() {
        return numFunction;
    }

    @Override
    public String toString() {
        return String.format("{end time: %1s, close price: %2$f, open price: %3$f, min price: %4$f, max price: %5$f, volume: %6$f}",
                             getEndTime().withZoneSameInstant(ZoneId.systemDefault()),
                             getClosePrice().doubleValue(),
                             getOpenPrice().doubleValue(),
                             getMinPrice().doubleValue(),
                             getMaxPrice().doubleValue(),
                             getVolume().doubleValue());
    }

    public static class Builder<T> extends BarBuilder<T> {
        private Builder(BarBuilderContext<T> context) {
            super(context);
        }

        public BaseOneSidedBar build(Duration timePeriod, ZonedDateTime endTime,
                                     T openPrice, T highPrice, T lowPrice, T closePrice,
                                     T volume, T amount) {
            return new BaseOneSidedBar(timePeriod, endTime,
                                       transform(openPrice), transform(highPrice), transform(lowPrice), transform(closePrice),
                                       transform(volume), transform(amount));
        }

        public static <T> Builder<T> create(BarBuilderContext<T> context) {
            return new Builder<>(context);
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

        @Override
        public Function<Number, Num> function() {
            return NaN().function();
        }

        @Override
        public Num getOpenPrice() {
            return NaN();
        }

        @Override
        public Num getMinPrice() {
            return NaN();
        }

        @Override
        public Num getMaxPrice() {
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
        public int getTrades() {
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
        public ZonedDateTime getBeginTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ZonedDateTime getEndTime() {
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
