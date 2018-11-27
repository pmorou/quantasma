package quantasma.core.analysis.parametrize;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Generator {
    Map<String, Parameter<?>> stateMap = new LinkedHashMap<>();

    private Generator() {
    }

    public static Generator instance() {
        return new Generator();
    }

    public Parameter<Integer> _int(String label) {
        final Parameter<Integer> value = new Parameter<>();
        if (!stateMap.isEmpty() && !stateMap.containsKey(label)) {
            lastValuesObject().nest(value);
        }
        stateMap.putIfAbsent(label, value);
        return (Parameter<Integer>) stateMap.get(label);
    }

    public Parameter<String> _String(String label) {
        final Parameter<String> value = new Parameter<>();
        if (!stateMap.isEmpty() && !stateMap.containsKey(label)) {
            lastValuesObject().nest(value);
        }
        stateMap.putIfAbsent(label, value);
        return (Parameter<String>) stateMap.get(label);
    }

    public <T> T next(Supplier<T> supplier) {
        if (stateMap.isEmpty()) {
            return supplier.get();
        }

        final Parameter<?> value = firstValuesObject();

        if (!value.next()) {
            throw new RuntimeException("No more options");
        }

        return supplier.get();
    }

    private Parameter<?> lastValuesObject() {
        Parameter<?> values = firstValuesObject();
        while (values.hasNested()) {
            values = values.nested();
        }
        return values;
    }

    private Parameter<?> firstValuesObject() {
        return stateMap.entrySet().iterator()
                       .next()
                       .getValue();
    }

}