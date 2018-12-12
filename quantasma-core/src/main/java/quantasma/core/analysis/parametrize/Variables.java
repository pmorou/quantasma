package quantasma.core.analysis.parametrize;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class Variables<P extends Enum & Parameterizable> {

    private final Map<String, Variable<?>> variablesByLabel = new LinkedHashMap<>();

    private Class<? extends Enum> parameterClass;

    private void checkEnum(P parameter) {
        if (parameterClass == null) {
            parameterClass = parameter.getClass();
        }
        if (parameter.getClass() != parameterClass) {
            throw new IllegalArgumentException(
                    String.format("Parameter type [%s] doesn't match - required [%s]", parameter.getClass(), parameterClass)); // what result
        }
    }

    public Variable<Integer> _int(P parameter) {
        checkEnum(parameter);
        return _int(parameter.name());
    }

    public Variable<Integer> _int(String label) {
        final Variable<Integer> param = new Variable<>();
        linkToLastVariable(label, param);
        return saveGet(label, param);
    }

    public Variable<String> _String(P parameter) {
        checkEnum(parameter);
        return _String(parameter.name());
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

    public boolean hasVariables() {
        return !variablesByLabel.isEmpty();
    }

    private boolean isDefined(String label) {
        return variablesByLabel.containsKey(label);
    }

    private Variable<?> lastVariable() {
        Variable<?> variable = firstVariable();
        while (variable.hasNextVariable()) {
            variable = variable.getNextVariable();
        }
        return variable;
    }

    public Variable<?> firstVariable() {
        return variablesByLabel.entrySet()
                               .iterator()
                               .next()
                               .getValue();
    }

    public <T> T produce(Function<Variables<P>, T> definition) {
        final Variable<?> variable = firstVariable();

        if (!variable.iterate()) {
            throw new NoSuchElementException();
        }

        return definition.apply(this);
    }

    public Values getValues() {
        return variablesByLabel.entrySet()
                               .stream()
                               .reduce(Values.of((Class<P>) parameterClass),
                                       (p, entry) -> p.add(entry.getKey(), entry.getValue().value()),
                                       Values::addAll);
    }
}
