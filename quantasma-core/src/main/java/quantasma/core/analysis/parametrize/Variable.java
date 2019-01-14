package quantasma.core.analysis.parametrize;

import quantasma.core.Iterables;
import quantasma.core.Iterables.ReusableIterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class Variable<T> {

    private final Set<T> allValues = new LinkedHashSet<>();

    private ReusableIterator<T> reusableIterator;
    private T currentValue;
    private Variable<?> nextVariable;

    Variable<?> getNextVariable() {
        return nextVariable;
    }

    void setNextVariable(Variable<?> variable) {
        this.nextVariable = variable;
    }

    public Variable<T> with(Collection<T> values) {
        return values(values);
    }

    public Variable<T> values(Collection<T> values) {
        if (isAlreadyIterating()) {
            return this;
        }
        allValues.addAll(values);
        return this;
    }

    @SafeVarargs
    public final Variable<T> with(T... values) {
        return values(values);
    }

    @SafeVarargs
    public final Variable<T> values(T... values) {
        if (isAlreadyIterating()) {
            return this;
        }
        allValues.addAll(Arrays.asList(values));
        return this;
    }

    private boolean isAlreadyIterating() {
        return currentValue != null;
    }

    boolean iterate() {
        return iterateOverThisVariable() || iterateOverNextVariable();
    }

    private boolean iterateOverThisVariable() {
        if (hasNext()) {
            updateCurrentValue();
            return true;
        }
        reusableIterator.reuse();
        updateCurrentValue();
        return false;
    }

    boolean hasNext() {
        return reusableIterator.hasNext();
    }

    private void updateCurrentValue() {
        currentValue = reusableIterator.next();
    }

    private boolean iterateOverNextVariable() {
        if (!hasNextVariable()) {
            return false;
        }
        return nextVariable.iterate();
    }

    boolean hasNextVariable() {
        return nextVariable != null;
    }

    public T $() {
        return value();
    }

    public T value() {
        if (currentValue == null) {
            reusableIterator = Iterables.reusableIterator(allValues);
            updateCurrentValue();
        }
        return currentValue;
    }

}