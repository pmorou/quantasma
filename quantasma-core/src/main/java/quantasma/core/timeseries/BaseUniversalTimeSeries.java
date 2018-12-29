package quantasma.core.timeseries;

import lombok.AccessLevel;
import lombok.Getter;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;
import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.BidAskBar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class BaseUniversalTimeSeries extends ForwardingTimeSeries implements UniversalTimeSeries {
    private final String symbol;
    private final BarPeriod barPeriod;

    protected BaseUniversalTimeSeries(Builder<?, ?> builder) {
        super(new BaseTimeSeries.SeriesBuilder()
                      .withName(builder.getName())
                      .withBars(builder.getBars())
                      .withNumTypeOf(builder.getNumFunction())
                      .withMaxBarCount(builder.getMaxBarCount())
                      .build());
        this.symbol = builder.getSymbol();
        this.barPeriod = builder.getBarPeriod();
    }

    @Override
    public Bar getBar(int i) {
        final int nthOldElement = getEndIndex() - i;

        if (nthOldElement < getBarCount()) {
            return delegate().getBar(i);
        }

        return BidAskBar.NaN;
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
