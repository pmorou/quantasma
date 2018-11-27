package quantasma.core.analysis.parametrize;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Generator {
    Map<String, Values<?>> stateMap = new LinkedHashMap<>();

    private Generator() {
    }

    public static Generator instance() {
        return new Generator();
    }

    public Values<Integer> _int(String label) {
        final Values<Integer> value = new Values<>();
        if (!stateMap.isEmpty() && !stateMap.containsKey(label)) {
            lastValuesObject().nest(value);
        }
        stateMap.putIfAbsent(label, value);
        return (Values<Integer>) stateMap.get(label);
    }

    public Values<String> _String(String label) {
        final Values<String> value = new Values<>();
        if (!stateMap.isEmpty() && !stateMap.containsKey(label)) {
            lastValuesObject().nest(value);
        }
        stateMap.putIfAbsent(label, value);
        return (Values<String>) stateMap.get(label);
    }

    public <T> T next(Supplier<T> supplier) {
        if (stateMap.isEmpty()) {
            return supplier.get();
        }

        final Values<?> value = firstValuesObject();

        if (!value.next()) {
            throw new RuntimeException("No more options");
        }

        return supplier.get();
    }

    private Values<?> lastValuesObject() {
        Values<?> values = firstValuesObject();
        while (values.hasNested()) {
            values = values.nested();
        }
        return values;
    }

    private Values<?> firstValuesObject() {
        return stateMap.entrySet().iterator()
                       .next()
                       .getValue();
    }

}