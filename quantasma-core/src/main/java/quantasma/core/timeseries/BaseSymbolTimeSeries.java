package quantasma.core.timeseries;

import lombok.AccessLevel;
import lombok.Getter;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;
import quantasma.core.BarPeriod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class BaseSymbolTimeSeries extends BaseTimeSeries implements SymbolTimeSeries {
    private final String symbol;
    private final BarPeriod barPeriod;

    protected BaseSymbolTimeSeries(String name, String symbol, BarPeriod barPeriod, int maxBarCount) {
        super(name);
        this.barPeriod = barPeriod;
        this.symbol = symbol;
        setMaximumBarCount(maxBarCount);
    }

    protected BaseSymbolTimeSeries(String name, String symbol, BarPeriod barPeriod) {
        this(name, symbol, barPeriod, Integer.MAX_VALUE);
    }

    protected BaseSymbolTimeSeries(Builder<?> builder) {
        super(builder.getName(), builder.getBars(), builder.getNumFunction());
        this.symbol = builder.getSymbol();
        this.barPeriod = builder.getBarPeriod();
        setMaximumBarCount(builder.getMaxBarCount());
    }

    @Getter(value = AccessLevel.PROTECTED)
    public static class Builder<T extends Builder<T>> {
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

        protected T self() {
            return (T) this;
        }

        public BaseSymbolTimeSeries build() {
            return new BaseSymbolTimeSeries(this);
        }

    }
}
