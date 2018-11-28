package quantasma.core.analysis.parametrize;

import java.util.Iterator;
import java.util.function.Function;

public interface Producer<T> extends Iterator<T> {

    Parameters getParameters();

    static <T> Producer<T> from(Function<Variables, T> recipe) {
        return new Producer<T>() {
            final private Variables variables = new Variables();

            private boolean isIterating;

            {
                recipe.apply(variables); // initialize variables
            }

            @Override
            public Parameters getParameters() {
                return variables.getParameters();
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
