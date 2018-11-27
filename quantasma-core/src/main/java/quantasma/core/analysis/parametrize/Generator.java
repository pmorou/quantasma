package quantasma.core.analysis.parametrize;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Generator {
    private final Map<String, Parameter<?>> parametersByLabel = new LinkedHashMap<>();

    private Generator() {
    }

    public static Generator instance() {
        return new Generator();
    }

    public Parameter<Integer> _int(String label) {
        final Parameter<Integer> param = new Parameter<>();
        linkLastElementWith(label, param);
        return saveGet(label, param);
    }

    public Parameter<String> _String(String label) {
        final Parameter<String> param = new Parameter<>();
        linkLastElementWith(label, param);
        return saveGet(label, param);
    }

    private <T> Parameter<T> saveGet(String label, Parameter<T> parameter) {
        parametersByLabel.putIfAbsent(label, parameter);
        return (Parameter<T>) parametersByLabel.get(label);
    }

    private <T> void linkLastElementWith(String label, Parameter<T> parameter) {
        if (hasElements() && !isDefined(label)) {
            lastParameter().getNextParameter(parameter);
        }
    }

    private boolean hasElements() {
        return !parametersByLabel.isEmpty();
    }

    private boolean isDefined(String label) {
        return parametersByLabel.containsKey(label);
    }

    public <T> T next(Supplier<T> supplier) {
        if (parametersByLabel.isEmpty()) {
            return supplier.get();
        }

        final Parameter<?> parameter = firstParameter();

        if (!parameter.iterate()) {
            throw new RuntimeException("No more options");
        }

        return supplier.get();
    }

    private Parameter<?> lastParameter() {
        Parameter<?> parameter = firstParameter();
        while (parameter.hasNextParameter()) {
            parameter = parameter.getNextParameter();
        }
        return parameter;
    }

    private Parameter<?> firstParameter() {
        return parametersByLabel.entrySet()
                                .iterator()
                                .next()
                                .getValue();
    }

}