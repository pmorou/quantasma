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

    public <T> Iterator<T> iterator(Supplier<T> supplier) {
        supplier.get();

        return new Iterator<T>() {
            private boolean firstRun = true;

            @Override
            public boolean hasNext() {
                if (firstRun) {
                    return true;
                }
                if (parametersByLabel.isEmpty()) {
                    return false;
                }
                Parameter<?> parameter = firstParameter();
                boolean hasNext = parameter.hasNext();
                while (!hasNext) {
                    parameter = parameter.getNextParameter();
                    if (parameter == null) {
                        return false;
                    }
                    hasNext = parameter.hasNext();
                }
                return hasNext;
            }

            @Override
            public T next() {
                if (firstRun) {
                    firstRun = false;
                    return supplier.get();
                }
                return generate(supplier);
            }
        };
    }

    private <T> T generate(Supplier<T> supplier) {
        if (parametersByLabel.isEmpty()) {
            return supplier.get();
        }

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

}