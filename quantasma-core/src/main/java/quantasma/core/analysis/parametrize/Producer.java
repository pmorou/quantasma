package quantasma.core.analysis.parametrize;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class Producer {
    private final Producer producer;

    private Map<String, Variable<?>> variablesByLabel = new LinkedHashMap<>();

    private Producer() {
        producer = this;
    }

    public static Producer instance() {
        return new Producer();
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

    private <T> T produce(Function<Producer, T> definition) {
        final Variable<?> variable = firstVariable();

        if (!variable.iterate()) {
            throw new NoSuchElementException();
        }

        return definition.apply(this);
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

    private void reset() {
        variablesByLabel = new LinkedHashMap<>();
    }

    public <T> Iterator<T> iterator(Function<Producer, T> recipe) {
        return new Iterator<T>() {
            private boolean isIterating;

            {
                producer.reset();
                recipe.apply(producer); // initialize variables
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
                    return recipe.apply(producer);
                }
                return produce(recipe);
            }
        };
    }
}