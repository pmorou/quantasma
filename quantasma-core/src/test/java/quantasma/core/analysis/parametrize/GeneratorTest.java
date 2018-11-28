package quantasma.core.analysis.parametrize;

import lombok.Data;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneratorTest {
    @Test
    public void givenOneValuesParameterShouldGenerateCorrectValues() {
        // given
        final Generator g = Generator.instance();
        Supplier<TestObject> supplier = () -> new TestObject(g._int("param1").values(1, 3, 5, 7).$());

        // when
        final Iterator<TestObject> iterator = g.iterator(supplier);

        // then
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().param1).isEqualTo(1);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().param1).isEqualTo(3);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().param1).isEqualTo(5);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().param1).isEqualTo(7);
        assertThat(iterator.hasNext()).isFalse();

        try {
            iterator.next();
        } catch (NoSuchElementException expected) {
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void givenTwoValuesParametersShouldGenerateCorrectValues() {
        // given
        final Generator g = Generator.instance();
        Supplier<TestObject> supplier = () -> new TestObject(g._int("param1").values(1, 3, 5).$(),
                                                             g._String("param2").values("a", "b", "c").$());

        // when
        final Iterator<TestObject> iterator = g.iterator(supplier);

        // then
        assertThat(iterator.hasNext()).isTrue();
        final TestObject _1thCall = iterator.next();
        assertThat(_1thCall.param1).isEqualTo(1);
        assertThat(_1thCall.param2).isEqualTo("a");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _2thCall = iterator.next();
        assertThat(_2thCall.param1).isEqualTo(3);
        assertThat(_2thCall.param2).isEqualTo("a");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _3thCall = iterator.next();
        assertThat(_3thCall.param1).isEqualTo(5);
        assertThat(_3thCall.param2).isEqualTo("a");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _4thCall = iterator.next();
        assertThat(_4thCall.param1).isEqualTo(1);
        assertThat(_4thCall.param2).isEqualTo("b");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _5thCall = iterator.next();
        assertThat(_5thCall.param1).isEqualTo(3);
        assertThat(_5thCall.param2).isEqualTo("b");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _6thCall = iterator.next();
        assertThat(_6thCall.param1).isEqualTo(5);
        assertThat(_6thCall.param2).isEqualTo("b");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _7thCall = iterator.next();
        assertThat(_7thCall.param1).isEqualTo(1);
        assertThat(_7thCall.param2).isEqualTo("c");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _8thCall = iterator.next();
        assertThat(_8thCall.param1).isEqualTo(3);
        assertThat(_8thCall.param2).isEqualTo("c");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _9thCall = iterator.next();
        assertThat(_9thCall.param1).isEqualTo(5);
        assertThat(_9thCall.param2).isEqualTo("c");

        assertThat(iterator.hasNext()).isFalse();

        try {
            iterator.next();
        } catch (NoSuchElementException expected) {
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void givenThreeValuesParametersShouldGenerateCorrectValues() {
        // given
        final Generator g = Generator.instance();
        Supplier<TestObject> supplier = () -> new TestObject(g._int("param1").values(1, 3).$(),
                                                             g._String("param2").values("a", "b", "c").$(),
                                                             g._int("param3").values(7, 9).$());

        // when
        final Iterator<TestObject> iterator = g.iterator(supplier);

        // then
        assertThat(iterator.hasNext()).isTrue();
        final TestObject _1thCall = iterator.next();
        assertThat(_1thCall.param1).isEqualTo(1);
        assertThat(_1thCall.param2).isEqualTo("a");
        assertThat(_1thCall.param3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _2thCall = iterator.next();
        assertThat(_2thCall.param1).isEqualTo(3);
        assertThat(_2thCall.param2).isEqualTo("a");
        assertThat(_2thCall.param3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _3thCall = iterator.next();
        assertThat(_3thCall.param1).isEqualTo(1);
        assertThat(_3thCall.param2).isEqualTo("b");
        assertThat(_3thCall.param3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _4thCall = iterator.next();
        assertThat(_4thCall.param1).isEqualTo(3);
        assertThat(_4thCall.param2).isEqualTo("b");
        assertThat(_4thCall.param3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _5thCall = iterator.next();
        assertThat(_5thCall.param1).isEqualTo(1);
        assertThat(_5thCall.param2).isEqualTo("c");
        assertThat(_5thCall.param3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _6thCall = iterator.next();
        assertThat(_6thCall.param1).isEqualTo(3);
        assertThat(_6thCall.param2).isEqualTo("c");
        assertThat(_6thCall.param3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _7thCall = iterator.next();
        assertThat(_7thCall.param1).isEqualTo(1);
        assertThat(_7thCall.param2).isEqualTo("a");
        assertThat(_7thCall.param3).isEqualTo(9);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _8thCall = iterator.next();
        assertThat(_8thCall.param1).isEqualTo(3);
        assertThat(_8thCall.param2).isEqualTo("a");
        assertThat(_8thCall.param3).isEqualTo(9);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _9thCall = iterator.next();
        assertThat(_9thCall.param1).isEqualTo(1);
        assertThat(_9thCall.param2).isEqualTo("b");
        assertThat(_9thCall.param3).isEqualTo(9);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _10thCall = iterator.next();
        assertThat(_10thCall.param1).isEqualTo(3);
        assertThat(_10thCall.param2).isEqualTo("b");
        assertThat(_10thCall.param3).isEqualTo(9);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _11thCall = iterator.next();
        assertThat(_11thCall.param1).isEqualTo(1);
        assertThat(_11thCall.param2).isEqualTo("c");
        assertThat(_11thCall.param3).isEqualTo(9);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _12thCall = iterator.next();
        assertThat(_12thCall.param1).isEqualTo(3);
        assertThat(_12thCall.param2).isEqualTo("c");
        assertThat(_12thCall.param3).isEqualTo(9);

        assertThat(iterator.hasNext()).isFalse();

        try {
            iterator.next();
        } catch (NoSuchElementException expected) {
            return;
        }
        throw new AssertionError();
    }

    @Data
    static class TestObject {
        private final int param1;
        private final String param2;
        private final int param3;

        TestObject(int param1) {
            this.param1 = param1;
            this.param2 = null;
            this.param3 = 0;
        }

        TestObject(int param1, String param2) {
            this.param1 = param1;
            this.param2 = param2;
            this.param3 = 0;
        }

        TestObject(int param1, String param2, int param3) {
            this.param1 = param1;
            this.param2 = param2;
            this.param3 = param3;
        }
    }

}