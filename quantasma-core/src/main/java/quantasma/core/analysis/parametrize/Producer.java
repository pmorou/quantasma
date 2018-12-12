package quantasma.core.analysis.parametrize;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Producer<T> {

    Stream<T> stream();

    Iterator<T> iterator();

    static <T, P extends Enum & Parameterizable> Producer<T> from(Function<Variables<P>, T> recipe) {
        return new SimpleProducer<>(recipe);
    }

    class SimpleProducer<T, P extends Enum & Parameterizable> implements Producer<T> {
        private final Function<Variables<P>, T> recipe;

        private SimpleProducer(Function<Variables<P>, T> recipe) {
            this.recipe = recipe;
        }

        @Override
        public Stream<T> stream() {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.DISTINCT), false);
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                final private Variables<P> variables = new Variables<>();

                private boolean isIterating;

                {
                    recipe.apply(variables); // initialize variables
                }

                @Override
                public boolean hasNext() {
                    if (!variables.hasVariables()) {
                        return false;
                    }
                    if (!isIterating) {
                        return true;
                    }

                    Variable<?> variable = variables.firstVariable();
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
                        return recipe.apply(variables);
                    }
                    return variables.produce(recipe);
                }
            };
        }
    }

}
