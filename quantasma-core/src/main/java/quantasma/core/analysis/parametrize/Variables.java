package quantasma.core.analysis.parametrize;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class Variables {

    private Map<String, Variable<?>> variablesByLabel = new LinkedHashMap<>();

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

    public <T> T produce(Function<Variables, T> definition) {
        final Variable<?> variable = firstVariable();

        if (!variable.iterate()) {
            throw new NoSuchElementException();
        }

        return definition.apply(this);
    }

}
