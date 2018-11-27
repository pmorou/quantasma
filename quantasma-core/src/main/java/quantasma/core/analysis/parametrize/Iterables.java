package quantasma.core.analysis.parametrize;

import java.util.Arrays;
import java.util.Iterator;

public class Iterables {

    public static <T> ReusableIterator<T> reusableIterator(T... values) {
        return new ReusableIteratorImpl<>(values);
    }

    interface ReusableIterator<T> extends Iterator<T> {
        void reuse();
    }

    static class ReusableIteratorImpl<T> implements ReusableIterator<T> {
        private final T[] values;

        private Iterator<T> iterator;

        private ReusableIteratorImpl(T[] values) {
            this.values = values;
            this.iterator = Arrays.asList(values).iterator();
        }

        @Override
        public void reuse() {
            iterator = Arrays.asList(values).iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            return iterator.next();
        }
    }

}
