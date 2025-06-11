package quantasma.core.timeseries;

import lombok.AccessLevel;
import lombok.Getter;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.num.DecimalNumFactory;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;
import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.BarFactory;
import quantasma.core.timeseries.bar.OneSidedBar;
import quantasma.core.timeseries.bar.OneSidedBarFactory;

import java.util.ArrayList;
import java.util.List;

public class BaseGenericTimeSeries<B extends OneSidedBar> implements GenericTimeSeries<B> {
    private final BarSeries barSeries;
    @Getter
    private final BarFactory<B> barFactory;
    @Getter
    private final String symbol;
    @Getter
    private final BarPeriod barPeriod;

    protected BaseGenericTimeSeries(Builder<?, ?> builder) {
        this.barSeries = new BaseBarSeriesBuilder()
            .withName(builder.getName())
            .withBars(builder.getBars())
            .withNumFactory(builder.getNumFactory())
            .withMaxBarCount(builder.getMaxBarCount())
            .build();
        this.barFactory = (BarFactory<B>) builder.getBarFactory();
        this.symbol = builder.getSymbol();
        this.barPeriod = builder.getBarPeriod();
    }

    @Override
    public BarSeries plainTimeSeries() {
        return new UnmodifiableTimeSeries(barSeries);
    }

    @Override
    public B getBar(int i) {
        final int nthOldElement = getEndIndex() - i;

        if (nthOldElement < getBarCount()) {
            return (B) barSeries.getBar(i);
        }

        return barFactory.getNaNBar();
    }

    @Override
    public void addBar(B bar, boolean replace) {
        barSeries.addBar(bar, replace);
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
    public String getName() {
        return barSeries.getName();
    }

    @Override
    public int getBarCount() {
        return barSeries.getBarCount();
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
    public NumFactory numFactory() {
        return barSeries.numFactory();
    }

    /**
     * Example of builder which preserves all methods of its parents<p>
     *
     * @param <T> Builder type
     * @param <R> {@code build()} return type
     */
    @Getter(value = AccessLevel.PROTECTED)
    public static class Builder<T extends Builder<T, R>, R extends BaseGenericTimeSeries> {
        private final String symbol;
        private final BarPeriod barPeriod;

        private String name = "unnamed_series";
        private List<Bar> bars = new ArrayList<>();
        private int maxBarCount = Integer.MAX_VALUE;
        private BarFactory<?> barFactory = new OneSidedBarFactory();
        private NumFactory numFactory = DecimalNumFactory.getInstance();

        public Builder(String symbol, BarPeriod barPeriod) {
            this.symbol = symbol;
            this.barPeriod = barPeriod;
        }

        public T withName(String name) {
            this.name = name;
            return self();
        }

        public T withNumFactory(NumFactory numFactory) {
            this.numFactory = numFactory;
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
            return (R) new BaseGenericTimeSeries(this);
        }

    }
}
