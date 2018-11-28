package quantasma.core;

import java.util.Arrays;
import java.util.Iterator;

public class Iterables {

    @SafeVarargs
    public static <T> ReusableIterator<T> reusableIterator(T... values) {
        return new ReusableIteratorImpl<>(Arrays.asList(values));
    }

    public static <T> ReusableIterator<T> reusableIterator(Iterable<T> values) {
        return new ReusableIteratorImpl<>(values);
    }

    public interface ReusableIterator<T> extends Iterator<T> {
        void reuse();
    }

    private static class ReusableIteratorImpl<T> implements ReusableIterator<T> {
        private final Iterable<T> values;

        private Iterator<T> iterator;

        private ReusableIteratorImpl(Iterable<T> values) {
            this.values = values;
            this.iterator = values.iterator();
        }

        @Override
        public void reuse() {
            iterator = values.iterator();
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
