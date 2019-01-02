package quantasma.core.timeseries;

import lombok.AccessLevel;
import lombok.Getter;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;
import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.BarFactory;
import quantasma.core.timeseries.bar.OneSidedBar;
import quantasma.core.timeseries.bar.OneSidedBarFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BaseUniversalTimeSeries<B extends OneSidedBar> implements UniversalTimeSeries<B> {
    private final TimeSeries timeSeries;
    @Getter
    private final BarFactory<B> barFactory;
    @Getter
    private final String symbol;
    @Getter
    private final BarPeriod barPeriod;

    protected BaseUniversalTimeSeries(Builder<?, ?> builder) {
        this.timeSeries = new BaseTimeSeries.SeriesBuilder()
                      .withName(builder.getName())
                      .withBars(builder.getBars())
                      .withNumTypeOf(builder.getNumFunction())
                      .withMaxBarCount(builder.getMaxBarCount())
                      .build();
        this.barFactory = (BarFactory<B>) builder.getBarFactory();
        this.symbol = builder.getSymbol();
        this.barPeriod = builder.getBarPeriod();
    }

    @Override
    public TimeSeries plainTimeSeries() {
        return new UnmodifiableTimeSeries(timeSeries);
    }

    @Override
    public B getBar(int i) {
        final int nthOldElement = getEndIndex() - i;

        if (nthOldElement < getBarCount()) {
            return (B) timeSeries.getBar(i);
        }

        return barFactory.createNaNBar();
    }

    @Override
    public void addBar(B bar, boolean replace) {
        timeSeries.addBar(bar, replace);
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
    public String getName() {
        return timeSeries.getName();
    }

    @Override
    public int getBarCount() {
        return timeSeries.getBarCount();
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
    public Num numOf(Number number) {
        return timeSeries.numOf(number);
    }

    @Override
    public Function<Number, Num> function() {
        return timeSeries.function();
    }

    /**
     * Example of builder which preserves all methods of its parents<p>
     *
     * @param <T> Builder type
     * @param <R> {@code build()} return type
     */
    @Getter(value = AccessLevel.PROTECTED)
    public static class Builder<T extends Builder<T, R>, R extends BaseUniversalTimeSeries> {
        private final String symbol;
        private final BarPeriod barPeriod;

        private String name = "unamed_series";
        private List<Bar> bars = new ArrayList<>();
        private int maxBarCount = Integer.MAX_VALUE;
        private BarFactory<?> barFactory = new OneSidedBarFactory();
        private Function<Number, Num> numFunction = PrecisionNum::valueOf;

        public Builder(String symbol, BarPeriod barPeriod) {
            this.symbol = symbol;
            this.barPeriod = barPeriod;
        }

        public T withName(String name) {
            this.name = name;
            return self();
        }

        public T withNumTypeOf(Function<Number, Num> numFunction) {
            this.numFunction = numFunction;
            return self();
        }

        public T withNumTypeOf(Num type) {
            this.numFunction = type.function();
            return self();
        }

        public T withBars(List<Bar> bars) {
            this.bars = bars;
            return self();
        }

        public T withMaxBarCount(int maxBarCount) {
            this.maxBarCount = maxBarCount;
            return self();
        }

        public T withBarFactory(BarFactory<?> barFactory) {
            this.barFactory = barFactory;
            return self();
        }

        /**
         * Every builder subclass should implement this method
         */
        protected T self() {
            return (T) this;
        }

        /**
         * Every builder subclass should implement this method
         */
        public R build() {
            return (R) new BaseUniversalTimeSeries(this);
        }

    }
}
