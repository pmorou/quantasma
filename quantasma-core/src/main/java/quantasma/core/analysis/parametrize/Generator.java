package quantasma.core.analysis.parametrize;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class Generator {
    private final Map<String, Variable<?>> variablesByLabel = new LinkedHashMap<>();

    private Generator() {
    }

    public static Generator instance() {
        return new Generator();
    }

    public Variable<Integer> _int(String label) {
        final Variable<Integer> param = new Variable<>();
        linkToLastVariable(label, param);
        return saveGet(label, param);
    }

    public Variable<String> _String(String label) {
        final Variable<String> param = new Variable<>();
        linkToLastVariable(label, param);
        return saveGet(label, param);
    }

    private <T> Variable<T> saveGet(String label, Variable<T> variable) {
        variablesByLabel.putIfAbsent(label, variable);
        return (Variable<T>) variablesByLabel.get(label);
    }

    private <T> void linkToLastVariable(String label, Variable<T> variable) {
        if (hasVariables() && !isDefined(label)) {
            lastVariable().setNextVariable(variable);
        }
    }

    private boolean hasVariables() {
        return !variablesByLabel.isEmpty();
    }

    private boolean isDefined(String label) {
        return variablesByLabel.containsKey(label);
    }

    private <T> T generate(Supplier<T> supplier) {
        final Variable<?> variable = firstVariable();

        if (!variable.iterate()) {
            throw new NoSuchElementException();
        }

        return supplier.get();
    }

    private Variable<?> lastVariable() {
        Variable<?> variable = firstVariable();
        while (variable.hasNextVariable()) {
            variable = variable.getNextVariable();
        }
        return variable;
    }

    private Variable<?> firstVariable() {
        return variablesByLabel.entrySet()
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
                if (variablesByLabel.isEmpty()) {
                    return false;
                }
                if (!isIterating) {
                    return true;
                }

                Variable<?> variable = firstVariable();
                boolean iterationFinished = false;
                while (variable != null && (iterationFinished = !variable.hasNext())) {
                    variable = variable.getNextVariable();
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