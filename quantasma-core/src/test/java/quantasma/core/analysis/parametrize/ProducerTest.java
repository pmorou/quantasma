package quantasma.core.analysis.parametrize;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ProducerTest {

    @Test
    public void givenNextElementsShouldReturnItsParameters() {
        // given
        final Function<Variables, TestObject> recipe = (var) -> new TestObject(var._int("var1").with(3, 1).$(),
                                                                               var._String("var2").with("a", "b").$());

        // when
        final Producer<TestObject> producer = Producer.from(recipe);

        // then
        assertThat(producer.hasNext()).isTrue();
        producer.next();
        assertThat(producer.getParameters().keys()).containsOnly("var1", "var2");
        assertThat(producer.getParameters().value("var1")).isEqualTo(3);
        assertThat(producer.getParameters().value("var2")).isEqualTo("a");

        assertThat(producer.hasNext()).isTrue();
        producer.next();
        assertThat(producer.getParameters().value("var1")).isEqualTo(1);
        assertThat(producer.getParameters().value("var2")).isEqualTo("a");
    }

    @Test
    public void givenOrderedValuesShouldProduceObjectsInTheSameOrder() {
        final Function<Variables, TestObject> recipe = (var) -> new TestObject(var._int("var1").with(3, 1, 7, 9).$());

        final Producer<TestObject> producer = Producer.from(recipe);

        assertThat(producer.next().var1).isEqualTo(3);
        assertThat(producer.next().var1).isEqualTo(1);
        assertThat(producer.next().var1).isEqualTo(7);
        assertThat(producer.next().var1).isEqualTo(9);
        assertThat(producer.hasNext()).isFalse();
    }

    @Test
    public void givenReusedVariablesShouldKeepTheSameValueForBoth() {
        final Function<Variables, TestObject> recipe = (var) -> {
            final Variable<Integer> var1 = var._int("var1").with(1, 2);
            final Variable<String> var2 = var._String("var2").with("9", "8");
            return new TestObject(var1.$(), var2.$(), var1.$());
        };

        final Producer<TestObject> producer = Producer.from(recipe);

        final TestObject _1stCall = producer.next();
        assertThat(_1stCall.var1).isEqualTo(1);
        assertThat(_1stCall.var2).isEqualTo("9");
        assertThat(_1stCall.var3).isEqualTo(1);

        final TestObject _2stCall = producer.next();
        assertThat(_2stCall.var1).isEqualTo(2);
        assertThat(_2stCall.var2).isEqualTo("9");
        assertThat(_2stCall.var3).isEqualTo(2);

        final TestObject _3stCall = producer.next();
        assertThat(_3stCall.var1).isEqualTo(1);
        assertThat(_3stCall.var2).isEqualTo("8");
        assertThat(_3stCall.var3).isEqualTo(1);

        final TestObject _4stCall = producer.next();
        assertThat(_4stCall.var1).isEqualTo(2);
        assertThat(_4stCall.var2).isEqualTo("8");
        assertThat(_4stCall.var3).isEqualTo(2);

        assertThat(producer.hasNext()).isFalse();
    }

    @Test
    public void givenDuplicatedValuesShouldProduceObjectsWithoutDuplicates() {
        final Function<Variables, TestObject> recipe = (var) -> new TestObject(var._int("var1").with(1, 2, 3, 2).$());

        final Producer<TestObject> producer = Producer.from(recipe);

        assertThat(producer.next().var1).isEqualTo(1);
        assertThat(producer.next().var1).isEqualTo(2);
        assertThat(producer.next().var1).isEqualTo(3);
        assertThat(producer.hasNext()).isFalse();
    }

    @Test
    public void givenSecondIteratorCallShouldReturnNewIterator() {
        // given
        final Function<Variables, TestObject> recipe = (var) -> new TestObject(var._int("var1").values(1, 3).$());
        final Iterator<TestObject> producer = Producer.from(recipe);
        for (int i = 0; i < 2; i++) {
            assertThat(producer.hasNext()).isTrue();
            producer.next();
        }
        assertThat(producer.hasNext()).isFalse();

        // when
        final Iterator<TestObject> nextProducer = Producer.from(recipe);

        // then
        for (int i = 0; i < 2; i++) {
            assertThat(nextProducer.hasNext()).isTrue();
            nextProducer.next();
        }
        assertThat(nextProducer.hasNext()).isFalse();
    }

    @Test
    public void given1VariablesShouldProduce4CorrectObjects() {
        // given
        final Function<Variables, TestObject> recipe = (var) -> new TestObject(var._int("var1").values(1, 3, 5, 7).$());

        // when
        final Producer<TestObject> producer = Producer.from(recipe);

        // then
        assertThat(producer.hasNext()).isTrue();
        assertThat(producer.next().var1).isEqualTo(1);
        assertThat(producer.hasNext()).isTrue();
        assertThat(producer.next().var1).isEqualTo(3);
        assertThat(producer.hasNext()).isTrue();
        assertThat(producer.next().var1).isEqualTo(5);
        assertThat(producer.hasNext()).isTrue();
        assertThat(producer.next().var1).isEqualTo(7);
        assertThat(producer.hasNext()).isFalse();

        try {
            producer.next();
        } catch (NoSuchElementException expected) {
            return;
        }
        fail();
    }

    @Test
    public void given2VariablesShouldProduceCorrectObjects() {
        // given
        final Function<Variables, TestObject> recipe = (var) -> new TestObject(var._int("var1").values(1, 3, 5).$(),
                                                                               var._String("var2").values("a", "b", "c").$());

        // when
        final Producer<TestObject> producer = Producer.from(recipe);

        // then
        assertThat(producer.hasNext()).isTrue();
        final TestObject _1thCall = producer.next();
        assertThat(_1thCall.var1).isEqualTo(1);
        assertThat(_1thCall.var2).isEqualTo("a");

        assertThat(producer.hasNext()).isTrue();
        final TestObject _2thCall = producer.next();
        assertThat(_2thCall.var1).isEqualTo(3);
        assertThat(_2thCall.var2).isEqualTo("a");

        assertThat(producer.hasNext()).isTrue();
        final TestObject _3thCall = producer.next();
        assertThat(_3thCall.var1).isEqualTo(5);
        assertThat(_3thCall.var2).isEqualTo("a");

        assertThat(producer.hasNext()).isTrue();
        final TestObject _4thCall = producer.next();
        assertThat(_4thCall.var1).isEqualTo(1);
        assertThat(_4thCall.var2).isEqualTo("b");

        assertThat(producer.hasNext()).isTrue();
        final TestObject _5thCall = producer.next();
        assertThat(_5thCall.var1).isEqualTo(3);
        assertThat(_5thCall.var2).isEqualTo("b");

        assertThat(producer.hasNext()).isTrue();
        final TestObject _6thCall = producer.next();
        assertThat(_6thCall.var1).isEqualTo(5);
        assertThat(_6thCall.var2).isEqualTo("b");

        assertThat(producer.hasNext()).isTrue();
        final TestObject _7thCall = producer.next();
        assertThat(_7thCall.var1).isEqualTo(1);
        assertThat(_7thCall.var2).isEqualTo("c");

        assertThat(producer.hasNext()).isTrue();
        final TestObject _8thCall = producer.next();
        assertThat(_8thCall.var1).isEqualTo(3);
        assertThat(_8thCall.var2).isEqualTo("c");

        assertThat(producer.hasNext()).isTrue();
        final TestObject _9thCall = producer.next();
        assertThat(_9thCall.var1).isEqualTo(5);
        assertThat(_9thCall.var2).isEqualTo("c");

        assertThat(producer.hasNext()).isFalse();

        try {
            producer.next();
        } catch (NoSuchElementException expected) {
            return;
        }
        fail();
    }

    @Test
    public void givenAlreadyStartedIteratorWhenUsedNewIteratorShouldBeAbleToContinueOldIterator() {
        // given
        Function<Variables, TestObject> recipe = var -> new TestObject(var._int("var1").values(1, 3).$());
        final Iterator<TestObject> it1 = Producer.from(recipe);
        assertThat(it1.hasNext()).isTrue();
        assertThat(it1.next().var1).isEqualTo(1);
        assertThat(it1.hasNext()).isTrue();

        // when
        final Iterator<TestObject> it2 = Producer.from(recipe);
        assertThat(it2.hasNext()).isTrue();
        assertThat(it2.next().var1).isEqualTo(1);
        assertThat(it2.hasNext()).isTrue();
        assertThat(it2.next().var1).isEqualTo(3);
        assertThat(it2.hasNext()).isFalse();

        // then
        assertThat(it1.hasNext()).isTrue();
        assertThat(it1.next().var1).isEqualTo(3);
        assertThat(it1.hasNext()).isFalse();
    }

    @Test
    public void given2VariablesShouldProduce12CorrectObjects() {
        // given
        final Function<Variables, TestObject> recipe = (var) -> new TestObject(var._int("var1").values(1, 3).$(),
                                                                               var._String("var2").values("a", "b", "c").$(),
                                                                               var._int("var3").values(7, 9).$());

        // when
        final Iterator<TestObject> producer = Producer.from(recipe);

        // then
        assertThat(producer.hasNext()).isTrue();
        final TestObject _1thCall = producer.next();
        assertThat(_1thCall.var1).isEqualTo(1);
        assertThat(_1thCall.var2).isEqualTo("a");
        assertThat(_1thCall.var3).isEqualTo(7);

        assertThat(producer.hasNext()).isTrue();
        final TestObject _2thCall = producer.next();
        assertThat(_2thCall.var1).isEqualTo(3);
        assertThat(_2thCall.var2).isEqualTo("a");
        assertThat(_2thCall.var3).isEqualTo(7);

        assertThat(producer.hasNext()).isTrue();
        final TestObject _3thCall = producer.next();
        assertThat(_3thCall.var1).isEqualTo(1);
        assertThat(_3thCall.var2).isEqualTo("b");
        assertThat(_3thCall.var3).isEqualTo(7);

        assertThat(producer.hasNext()).isTrue();
        final TestObject _4thCall = producer.next();
        assertThat(_4thCall.var1).isEqualTo(3);
        assertThat(_4thCall.var2).isEqualTo("b");
        assertThat(_4thCall.var3).isEqualTo(7);

        assertThat(producer.hasNext()).isTrue();
        final TestObject _5thCall = producer.next();
        assertThat(_5thCall.var1).isEqualTo(1);
        assertThat(_5thCall.var2).isEqualTo("c");
        assertThat(_5thCall.var3).isEqualTo(7);

        assertThat(producer.hasNext()).isTrue();
        final TestObject _6thCall = producer.next();
        assertThat(_6thCall.var1).isEqualTo(3);
        assertThat(_6thCall.var2).isEqualTo("c");
        assertThat(_6thCall.var3).isEqualTo(7);

        assertThat(producer.hasNext()).isTrue();
        final TestObject _7thCall = producer.next();
        assertThat(_7thCall.var1).isEqualTo(1);
        assertThat(_7thCall.var2).isEqualTo("a");
        assertThat(_7thCall.var3).isEqualTo(9);

        assertThat(producer.hasNext()).isTrue();
        final TestObject _8thCall = producer.next();
        assertThat(_8thCall.var1).isEqualTo(3);
        assertThat(_8thCall.var2).isEqualTo("a");
        assertThat(_8thCall.var3).isEqualTo(9);

        assertThat(producer.hasNext()).isTrue();
        final TestObject _9thCall = producer.next();
        assertThat(_9thCall.var1).isEqualTo(1);
        assertThat(_9thCall.var2).isEqualTo("b");
        assertThat(_9thCall.var3).isEqualTo(9);

        assertThat(producer.hasNext()).isTrue();
        final TestObject _10thCall = producer.next();
        assertThat(_10thCall.var1).isEqualTo(3);
        assertThat(_10thCall.var2).isEqualTo("b");
        assertThat(_10thCall.var3).isEqualTo(9);

        assertThat(producer.hasNext()).isTrue();
        final TestObject _11thCall = producer.next();
        assertThat(_11thCall.var1).isEqualTo(1);
        assertThat(_11thCall.var2).isEqualTo("c");
        assertThat(_11thCall.var3).isEqualTo(9);

        assertThat(producer.hasNext()).isTrue();
        final TestObject _12thCall = producer.next();
        assertThat(_12thCall.var1).isEqualTo(3);
        assertThat(_12thCall.var2).isEqualTo("c");
        assertThat(_12thCall.var3).isEqualTo(9);

        assertThat(producer.hasNext()).isFalse();

        try {
            producer.next();
        } catch (NoSuchElementException expected) {
            return;
        }
        fail();
    }

    static class TestObject {
        private final int var1;
        private final String var2;
        private final int var3;

        TestObject(int var1) {
            this.var1 = var1;
            this.var2 = null;
            this.var3 = 0;
        }

        TestObject(int var1, String var2) {
            this.var1 = var1;
            this.var2 = var2;
            this.var3 = 0;
        }

        TestObject(int var1, String var2, int var3) {
            this.var1 = var1;
            this.var2 = var2;
            this.var3 = var3;
        }
    }

}