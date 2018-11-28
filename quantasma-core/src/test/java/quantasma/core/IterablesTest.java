package quantasma.core;

import org.junit.Test;
import quantasma.core.Iterables.ReusableIterator;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IterablesTest {
    @Test
    public void testReusableIterator() {
        // given
        final ReusableIterator<Integer> reusableIterator = Iterables.reusableIterator(1, 3, 5);

        // when 1st round
        List<Integer> values = new ArrayList<>();
        while (reusableIterator.hasNext()) {
            values.add(reusableIterator.next());
        }

        // then
        assertThat(values).hasSize(3).containsExactly(1, 3, 5);

        // reuse
        reusableIterator.reuse();

        // when 2nd round
        values.clear();
        while (reusableIterator.hasNext()) {
            values.add(reusableIterator.next());
        }

        // then
        assertThat(values).hasSize(3).containsExactly(1, 3, 5);
    }
}