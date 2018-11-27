package quantasma.core.analysis.parametrize;

import quantasma.core.Iterables;
import quantasma.core.Iterables.ReusableIterator;

public class Parameter<T> {

    private ReusableIterator<T> reusableIterator;
    private T currentValue;
    private Parameter<?> nextParameter;

    public Parameter<?> getNextParameter() {
        return nextParameter;
    }

    public void getNextParameter(Parameter<?> parameter) {
        this.nextParameter = parameter;
    }

    public Parameter<T> values(T... values) {
        if (isAlreadyUsed()) {
            return this;
        }
        reusableIterator = Iterables.reusableIterator(values);
        return this;
    }

    private boolean isAlreadyUsed() {
        return currentValue != null;
    }

    boolean iterate() {
        return overThisParameter() || overNextParameter();
    }

    public boolean overThisParameter() {
        if (reusableIterator.hasNext()) {
            updateCurrentValue();
            return true;
        }
        reusableIterator.reuse();
        updateCurrentValue();
        return false;
    }

    private void updateCurrentValue() {
        currentValue = reusableIterator.next();
    }

    public boolean overNextParameter() {
        if (!hasNextParameter()) {
            return false;
        }
        return nextParameter.iterate();
    }

    public boolean hasNextParameter() {
        return nextParameter != null;
    }

    public T $() {
        if (currentValue == null) {
            updateCurrentValue();
        }
        return currentValue;
    }

}