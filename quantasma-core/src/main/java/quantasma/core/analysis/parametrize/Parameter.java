package quantasma.core.analysis.parametrize;

import quantasma.core.Iterables;
import quantasma.core.Iterables.ReusableIterator;

public class Parameter<T> {

    private ReusableIterator<T> reusableIterator;
    private T currentValue;
    private Parameter<?> nextParameter;

    public boolean hasNextParameter() {
        return nextParameter != null;
    }

    public Parameter<?> nextParameter() {
        return nextParameter;
    }

    public boolean loadedNextParametersValue() {
        if (!hasNextParameter()) {
            return false;
        }
        if (nextParameter.hasNext()) {
            nextParameter.next();
            return true;
        }
        nextParameter.reset();
        nextParameter.next();
        return false;
    }

    public boolean hasNext() {
        return reusableIterator.hasNext();
    }

    public void reset() {
        reusableIterator.reuse();
    }

    public void nextParameter(Parameter<?> parameter) {
        this.nextParameter = parameter;
    }

    public Parameter<T> values(T... values) {
        if (isAlreadyRunning()) {
            return this;
        }
        reusableIterator = Iterables.reusableIterator(values);
        return this;
    }

    private boolean isAlreadyRunning() {
        return currentValue != null;
    }

    boolean next() {
        return loadedNextParametersValue() || loadedNextValue();
    }

    public boolean loadedNextValue() {
        if (hasNext()) {
            getNext();
            return true;
        }
        return false;
    }

    public T $() {
        if (currentValue == null) {
            getNext();
        }
        return currentValue;
    }

    private void getNext() {
        currentValue = reusableIterator.next();
    }

}