package quantasma.core.analysis.parametrize;

import quantasma.core.Iterables;
import quantasma.core.Iterables.ReusableIterator;

public class Variable<T> {

    private ReusableIterator<T> reusableIterator;
    private T currentValue;
    private Variable<?> nextVariable;

    Variable<?> getNextVariable() {
        return nextVariable;
    }

    void setNextVariable(Variable<?> variable) {
        this.nextVariable = variable;
    }

    public Variable<T> values(T... values) {
        if (isAlreadyGenerated()) {
            return this;
        }
        reusableIterator = Iterables.reusableIterator(values);
        return this;
    }

    private boolean isAlreadyGenerated() {
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
            updateCurrentValue();
        }
        return currentValue;
    }

}