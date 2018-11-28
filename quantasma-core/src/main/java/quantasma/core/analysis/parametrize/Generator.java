package quantasma.core.analysis.parametrize;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
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
            lastParameter().setNextParameter(parameter);
        }
    }

    private boolean hasElements() {
        return !parametersByLabel.isEmpty();
    }

    private boolean isDefined(String label) {
        return parametersByLabel.containsKey(label);
    }

    private <T> T generate(Supplier<T> supplier) {
        final Parameter<?> parameter = firstParameter();

        if (!parameter.iterate()) {
            throw new NoSuchElementException();
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

    public <T> Iterator<T> iterator(Supplier<T> supplier) {
        return new Iterator<T>() {
            private boolean isIterating;

            {
                supplier.get(); // initialize values
            }

            @Override
            public boolean hasNext() {
                if (parametersByLabel.isEmpty()) {
                    return false;
                }
                if (!isIterating) {
                    return true;
                }

                Parameter<?> parameter = firstParameter();
                boolean iterationFinished = false;
                while (parameter != null && (iterationFinished = !parameter.hasNext())) {
                    parameter = parameter.getNextParameter();
                }
                return !iterationFinished;
            }

            @Override
            public T next() {
                if (!isIterating) {
                    isIterating = true;
                    return supplier.get();
                }
                return generate(supplier);
            }
        };
    }
}