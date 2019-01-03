package quantasma.core.timeseries.bar.generic;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.function.Function;

public class Argument<T> {
    @Getter
    private Function<T, Number> toNumber;

    private Argument(Function<T, Number> toNumber) {
        this.toNumber = toNumber;
    }

    public static <T> Argument<T> of(Function<T, Number> toNumber) {
        return new Argument<>(toNumber);
    }

    public static Argument<String> ofString() {
        return of(BigDecimal::new);
    }

    public static Argument<Double> ofDouble() {
        return of(o -> o);
    }

    public static Argument<Integer> ofInteger() {
        return of(o -> o);
    }

    public static <T extends Number> Argument<T> of(Class<T> numberClass) {
        return of(o -> o);
    }
}