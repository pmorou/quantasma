package quantasma.core.analysis.parametrize;

import quantasma.core.analysis.parametrize.Iterables.ReusableIterator;

public class Parameter<T> {

    private ReusableIterator<T> reusableIterator;
    private T value;

    private Parameter<?> nestedValue;

    public boolean hasNested() {
        return nestedValue != null;
    }

    public Parameter<?> nested() {
        return nestedValue;
    }

    public boolean loadedNestedNext() {
        if (!hasNested()) {
            return false;
        }
        if (nestedValue.hasNext()) {
            nestedValue.next();
            return true;
        }
        nestedValue.reset();
        nestedValue.next();
        return false;
    }

    public boolean hasNext() {
        return reusableIterator.hasNext();
    }

    public void reset() {
        reusableIterator.reuse();
    }

    public void nest(Parameter<?> nestedValues) {
        this.nestedValue = nestedValues;
    }

    public Parameter<T> values(T... values) {
        if (isAlreadyRunning()) {
            return this;
        }
        reusableIterator = quantasma.core.analysis.parametrize.Iterables.reusableIterator(values);
        return this;
    }

    private boolean isAlreadyRunning() {
        return value != null;
    }

    boolean next() {
        return loadedNestedNext() || loadedNext();
    }

    public boolean loadedNext() {
        if (hasNext()) {
            getNext();
            return true;
        }
        return false;
    }

    public T $() {
        if (value == null) {
            getNext();
        }
        return value;
    }

    private void getNext() {
        value = reusableIterator.next();
    }

}