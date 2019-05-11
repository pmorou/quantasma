package quantasma.core.analysis.parametrize

import spock.lang.FailsWith
import spock.lang.Specification

import java.util.function.Function

class ProducerSpec extends Specification {

    def 'given ordered values should produce objects in the same order'() {
        given:
        final Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").with(3, 1, 7, 9).$()) }

        when:
        final Iterator<TestObject> iterator = Producer.from(recipe).iterator()

        then:
        with(iterator) {
            next().var1 == 3
            next().var1 == 1
            next().var1 == 7
            next().var1 == 9
            !hasNext()
        }
    }

    def 'given reused variables should keep the same value for both'() {
        given:
        final Function<Variables, TestObject> recipe = { var ->
            final Variable<Integer> var1 = var._int("var1").with(1, 2)
            final Variable<String> var2 = var._String("var2").with("9", "8")
            return new TestObject(var1.$(), var2.$(), var1.$())
        }

        when:
        final Iterator<TestObject> iterator = Producer.from(recipe).iterator()

        then:
        with(iterator) {
            assertFields(next(), 1, "9", 1)
            assertFields(next(), 2, "9", 2)
            assertFields(next(), 1, "8", 1)
            assertFields(next(), 2, "8", 2)
            !hasNext()
        }
    }

    def 'given duplicated values should produce objects without duplicates'() {
        given:
        final Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").with(1, 2, 3, 2).$()) }

        when:
        final Iterator<TestObject> iterator = Producer.from(recipe).iterator()

        then:
        with(iterator) {
            next().var1 == 1
            next().var1 == 2
            next().var1 == 3
            !hasNext()
        }
    }

    def 'given second iterator call should return new iterator'() {
        given:
        final Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").values(1, 3).$()) }
        final Producer<TestObject> producer = Producer.from(recipe)
        final Iterator<TestObject> iterator = producer.iterator()

        and:
        for (int i = 0; i < 2; i++) {
            assert iterator.hasNext()
            iterator.next()
        }
        assert !iterator.hasNext()

        when:
        final Iterator<TestObject> nextIterator = Producer.from(recipe).iterator()

        then:
        for (int i = 0; i < 2; i++) {
            with(nextIterator) {
                hasNext()
                next()
            }
        }
        !nextIterator.hasNext()
    }

    @FailsWith(NoSuchElementException)
    def 'given 1 variables should produce 4 correct objects'() {
        given:
        final Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").values(1, 3, 5, 7).$()) }

        when:
        final Iterator<TestObject> iterator = Producer.from(recipe).iterator()

        then:
        with(iterator) {
            hasNext()
            next().var1 == 1
            hasNext()
            next().var1 == 3
            hasNext()
            next().var1 == 5
            hasNext()
            next().var1 == 7
            !hasNext()
            next()
        }
    }

    @FailsWith(NoSuchElementException)
    def 'given 2 variables should produce correct objects'() {
        given:
        final Function<Variables, TestObject> recipe = { var ->
            new TestObject(var._int("var1").values(1, 3, 5).$(),
                    var._String("var2").values("a", "b", "c").$())
        }

        when:
        final Iterator<TestObject> iterator = Producer.from(recipe).iterator()

        then:
        with(iterator) {
            hasNext()
            assertFields(next(), 1, "a")
            hasNext()
            assertFields(next(), 3, "a")
            hasNext()
            assertFields(next(), 5, "a")
            hasNext()
            assertFields(next(), 1, "b")
            hasNext()
            assertFields(next(), 3, "b")
            hasNext()
            assertFields(next(), 5, "b")
            hasNext()
            assertFields(next(), 1, "c")
            hasNext()
            assertFields(next(), 3, "c")
            hasNext()
            assertFields(next(), 5, "c")
            !hasNext()
            next() // throws an exception
        }
    }

    def 'given already started iterator when used new iterator should be able to continue old iterator'() {
        given:
        final Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").values(1, 3).$()) }
        final Producer producer = Producer.from(recipe)
        final Iterator<TestObject> oldIterator = producer.iterator()
        with(oldIterator) {
            hasNext()
            next().var1 == 1
            hasNext()
        }

        when:
        final Iterator<TestObject> newIterator = producer.iterator()
        with(newIterator) {
            hasNext()
            next().var1 == 1
            hasNext()
            next().var1 == 3
            !hasNext()
        }

        then:
        with(oldIterator) {
            hasNext()
            next().var1 == 3
            !hasNext()
        }
    }

    @FailsWith(NoSuchElementException)
    def 'given 2 variables should produce 12 correct objects'() {
        given:
        final Function<Variables, TestObject> recipe = { var ->
            new TestObject(var._int("var1").values(1, 3).$(),
                    var._String("var2").values("a", "b", "c").$(),
                    var._int("var3").values(7, 9).$())
        }

        when:
        final Iterator<TestObject> iterator = Producer.from(recipe).iterator()

        then:
        with(iterator) {
            hasNext()
            assertFields(next(), 1, "a", 7)
            hasNext()
            assertFields(next(), 3, "a", 7)
            hasNext()
            assertFields(next(), 1, "b", 7)
            hasNext()
            assertFields(next(), 3, "b", 7)
            hasNext()
            assertFields(next(), 1, "c", 7)
            hasNext()
            assertFields(next(), 3, "c", 7)
            hasNext()
            assertFields(next(), 1, "a", 9)
            hasNext()
            assertFields(next(), 3, "a", 9)
            hasNext()
            assertFields(next(), 1, "b", 9)
            hasNext()
            assertFields(next(), 3, "b", 9)
            hasNext()
            assertFields(next(), 1, "c", 9)
            hasNext()
            assertFields(next(), 3, "c", 9)
            !hasNext()
            next()
        }
    }

    private void assertFields(TestObject testObject, int expectedVar1, String expectedVar2) {
        assertFields(testObject, expectedVar1, expectedVar2, 0)
    }

    private void assertFields(TestObject testObject, int expectedVar1, String expectedVar2, Integer expectedVar3) {
        verifyAll(testObject) {
            var1 == expectedVar1
            var2 == expectedVar2
            var3 == expectedVar3
        }
    }

    static class TestObject {
        private final int var1
        private final String var2
        private final int var3

        TestObject(int var1) {
            this.var1 = var1
            this.var2 = null
            this.var3 = 0
        }

        TestObject(int var1, String var2) {
            this.var1 = var1
            this.var2 = var2
            this.var3 = 0
        }

        TestObject(int var1, String var2, int var3) {
            this.var1 = var1
            this.var2 = var2
            this.var3 = var3
        }
    }

}