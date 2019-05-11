package quantasma.core.timeseries.bar.generic;

import org.ta4j.core.num.Num;

import java.util.function.Function;

public abstract class GenericNumMethod<T> {
    private final Function<Number, Num> numFunction;
    private final Argument<T> context;

    protected GenericNumMethod(Function<Number, Num> numFunction, Argument<T> context) {
        this.numFunction = numFunction;
        this.context = context;
    }

    protected final Num transform(T value) {
        return context.getToNumber()
            .andThen(numFunction)
            .apply(value);
    }

}
