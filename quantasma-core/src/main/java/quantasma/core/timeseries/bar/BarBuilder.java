package quantasma.core.timeseries.bar;

import org.ta4j.core.num.Num;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;

public abstract class BarBuilder<T> {
    private final BarBuilderContext<T> context;

    protected BarBuilder(BarBuilderContext<T> context) {
        this.context = context;
    }

    final protected Num transform(T value) {
        return context.transform(value);
    }

    public static BarBuilderContext.ArgumentType create(Function<Number, Num> numFunction) {
        return new BarBuilderContext.ArgumentType(numFunction);
    }

    public static class BarBuilderContext<T> {
        private Function<T, Number> toNumber;
        private Function<Number, Num> numberToNum;

        private BarBuilderContext(Function<T, Number> toNumber, Function<Number, Num> numFunction) {
            this.toNumber = toNumber;
            this.numberToNum = numFunction;
        }

        final protected Num transform(T openPrice) {
            return toNumber.andThen(numberToNum)
                           .apply(openPrice);
        }

        public static class ArgumentType {
            private final Function<Number, Num> numFunction;

            private ArgumentType(Function<Number, Num> numFunction) {
                this.numFunction = Objects.requireNonNull(numFunction);
            }

            private <T> BarBuilderContext<T> from(Function<T, Number> toNumber,
                                                  Function<Number, Num> numFunction) {
                return new BarBuilderContext<>(toNumber, numFunction);
            }

            public BarBuilderContext<String> fromString() {
                return from(BigDecimal::new, numFunction);
            }

            public BarBuilderContext<Double> fromDouble() {
                return from(o -> o, numFunction);
            }

            public BarBuilderContext<Integer> fromInteger() {
                return from(o -> o, numFunction);
            }

            public BarBuilderContext<Num> fromNum() {
                return from(Num::doubleValue, numFunction);
            }

        }

    }
}
