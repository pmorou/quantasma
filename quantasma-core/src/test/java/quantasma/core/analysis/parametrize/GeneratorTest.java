package quantasma.core.analysis.parametrize;

import lombok.Data;
import org.junit.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneratorTest {
    @Test
    public void givenOneParameterShouldGenerateCorrectValues() {
        // given
        final Generator g = Generator.instance();
        Supplier<TestObject> supplier = () -> new TestObject(g._int("param1").values(1, 3, 5, 7).$());

        // then
        assertThat(g.next(supplier).param1).isEqualTo(1);
        assertThat(g.next(supplier).param1).isEqualTo(3);
        assertThat(g.next(supplier).param1).isEqualTo(5);
        assertThat(g.next(supplier).param1).isEqualTo(7);

        try {
            g.next(supplier);
        } catch (RuntimeException e) {
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void givenTwoParametersShouldGenerateCorrectValues() {
        // given
        final Generator g = Generator.instance();
        Supplier<TestObject> supplier = () -> new TestObject(g._int("param1").values(1, 3, 5).$(),
                                                        g._String("param2").values("a", "b", "c").$());

        // then
        final TestObject _1thCall = g.next(supplier);
        assertThat(_1thCall.param1).isEqualTo(1);
        assertThat(_1thCall.param2).isEqualTo("a");

        final TestObject _2thCall = g.next(supplier);
        assertThat(_2thCall.param1).isEqualTo(1);
        assertThat(_2thCall.param2).isEqualTo("b");

        final TestObject _3thCall = g.next(supplier);
        assertThat(_3thCall.param1).isEqualTo(1);
        assertThat(_3thCall.param2).isEqualTo("c");

        final TestObject _4thCall = g.next(supplier);
        assertThat(_4thCall.param1).isEqualTo(3);
        assertThat(_4thCall.param2).isEqualTo("a");

        final TestObject _5thCall = g.next(supplier);
        assertThat(_5thCall.param1).isEqualTo(3);
        assertThat(_5thCall.param2).isEqualTo("b");

        final TestObject _6thCall = g.next(supplier);
        assertThat(_6thCall.param1).isEqualTo(3);
        assertThat(_6thCall.param2).isEqualTo("c");

        final TestObject _7thCall = g.next(supplier);
        assertThat(_7thCall.param1).isEqualTo(5);
        assertThat(_7thCall.param2).isEqualTo("a");

        final TestObject _8thCall = g.next(supplier);
        assertThat(_8thCall.param1).isEqualTo(5);
        assertThat(_8thCall.param2).isEqualTo("b");

        final TestObject _9thCall = g.next(supplier);
        assertThat(_9thCall.param1).isEqualTo(5);
        assertThat(_9thCall.param2).isEqualTo("c");

        try {
            g.next(supplier);
        } catch (RuntimeException e) {
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void givenThreeParametersShouldGenerateCorrectValues() {
        // given
        final Generator g = Generator.instance();
        Supplier<TestObject> supplier = () -> new TestObject(g._int("param1").values(1, 3).$(),
                                                        g._String("param2").values("a", "b", "c").$(),
                                                        g._int("param3").values(7, 9).$());

        // then
        final TestObject _1thCall = g.next(supplier);
        assertThat(_1thCall.param1).isEqualTo(1);
        assertThat(_1thCall.param2).isEqualTo("a");
        assertThat(_1thCall.param3).isEqualTo(7);

        final TestObject _2thCall = g.next(supplier);
        assertThat(_2thCall.param1).isEqualTo(1);
        assertThat(_2thCall.param2).isEqualTo("a");
        assertThat(_2thCall.param3).isEqualTo(9);

        final TestObject _3thCall = g.next(supplier);
        assertThat(_3thCall.param1).isEqualTo(1);
        assertThat(_3thCall.param2).isEqualTo("b");
        assertThat(_3thCall.param3).isEqualTo(7);

        final TestObject _4thCall = g.next(supplier);
        assertThat(_4thCall.param1).isEqualTo(1);
        assertThat(_4thCall.param2).isEqualTo("b");
        assertThat(_4thCall.param3).isEqualTo(9);

        final TestObject _5thCall = g.next(supplier);
        assertThat(_5thCall.param1).isEqualTo(1);
        assertThat(_5thCall.param2).isEqualTo("c");
        assertThat(_5thCall.param3).isEqualTo(7);

        final TestObject _6thCall = g.next(supplier);
        assertThat(_6thCall.param1).isEqualTo(1);
        assertThat(_6thCall.param2).isEqualTo("c");
        assertThat(_6thCall.param3).isEqualTo(9);

        final TestObject _7thCall = g.next(supplier);
        assertThat(_7thCall.param1).isEqualTo(3);
        assertThat(_7thCall.param2).isEqualTo("a");
        assertThat(_7thCall.param3).isEqualTo(7);

        try {
            g.next(supplier);
        } catch (RuntimeException e) {
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