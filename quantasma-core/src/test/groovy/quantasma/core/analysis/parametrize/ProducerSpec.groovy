package quantasma.core.analysis.parametrize

import spock.lang.FailsWith
import spock.lang.Specification

import java.util.function.Function

class ProducerSpec extends Specification {

    def 'given next elements should return its parameters'() {
        given:
        final Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").with(3, 1).$(),
                                                                               var._String("var2").with("a", "b").$()) }

        when:
        final Producer<TestObject> producer = Producer.from(recipe)

        then:
        producer.hasNext()
        producer.next()
        producer.getParameters().keys().every({ it in ["var1", "var2"] })
        producer.getParameters().value("var1") == 3
        producer.getParameters().value("var2") == "a"

        producer.hasNext()
        producer.next()
        producer.getParameters().value("var1") == 1
        producer.getParameters().value("var2") == "a"
    }

    def 'given ordered values should produce objects in the same order'() {
        given:
        final Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").with(3, 1, 7, 9).$()) }

        when:
        final Producer<TestObject> producer = Producer.from(recipe)

        then:
        producer.next().var1 == 3
        producer.next().var1 == 1
        producer.next().var1 == 7
        producer.next().var1 == 9
        !producer.hasNext()
    }

    def 'given reused variables should keep the same value for both'() {
        given:
        final Function<Variables, TestObject> recipe = { var ->
            final Variable<Integer> var1 = var._int("var1").with(1, 2)
            final Variable<String> var2 = var._String("var2").with("9", "8")
            return new TestObject(var1.$(), var2.$(), var1.$())
         }

        when:
        final Producer<TestObject> producer = Producer.from(recipe)

        then:
        final TestObject _1stCall = producer.next()
        _1stCall.var1 == 1
        _1stCall.var2 == "9"
        _1stCall.var3 == 1

        final TestObject _2stCall = producer.next()
        _2stCall.var1 == 2
        _2stCall.var2 == "9"
        _2stCall.var3 == 2

        final TestObject _3stCall = producer.next()
        _3stCall.var1 == 1
        _3stCall.var2 == "8"
        _3stCall.var3 == 1

        final TestObject _4stCall = producer.next()
        _4stCall.var1 == 2
        _4stCall.var2 == "8"
        _4stCall.var3 == 2

        !producer.hasNext()
    }

    def 'given duplicated values should produce objects without duplicates'() {
        given:
        final Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").with(1, 2, 3, 2).$()) }

        when:
        final Producer<TestObject> producer = Producer.from(recipe)

        then:
        producer.next().var1 == 1
        producer.next().var1 == 2
        producer.next().var1 == 3
        !producer.hasNext()
    }

    def 'given second iterator call should return new iterator'() {
        given:
        final Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").values(1, 3).$()) }
        final Iterator<TestObject> producer = Producer.from(recipe)
        for (int i = 0; i < 2; i++) {
            producer.hasNext()
            producer.next()
        }
        !producer.hasNext()

        when:
        final Iterator<TestObject> nextProducer = Producer.from(recipe)

        then:
        for (int i = 0; i < 2; i++) {
            nextProducer.hasNext()
            nextProducer.next()
        }
        !nextProducer.hasNext()
    }

    @FailsWith(NoSuchElementException)
    def 'given 1 variables should produce 4 correct objects'() {
        given:
        final Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").values(1, 3, 5, 7).$()) }

        when:
        final Producer<TestObject> producer = Producer.from(recipe)

        then:
        producer.hasNext()
        producer.next().var1 == 1
        producer.hasNext()
        producer.next().var1 == 3
        producer.hasNext()
        producer.next().var1 == 5
        producer.hasNext()
        producer.next().var1 == 7
        !producer.hasNext()
        producer.next()
    }

    @FailsWith(NoSuchElementException)
    def 'given 2 variables should produce correct objects'() {
        given:
        final Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").values(1, 3, 5).$(),
                                                                               var._String("var2").values("a", "b", "c").$()) }

        when:
        final Producer<TestObject> producer = Producer.from(recipe)

        then:
        producer.hasNext()
        final TestObject _1thCall = producer.next()
        _1thCall.var1 == 1
        _1thCall.var2 == "a"

        producer.hasNext()
        final TestObject _2thCall = producer.next()
        _2thCall.var1 == 3
        _2thCall.var2 == "a"

        producer.hasNext()
        final TestObject _3thCall = producer.next()
        _3thCall.var1 == 5
        _3thCall.var2 == "a"

        producer.hasNext()
        final TestObject _4thCall = producer.next()
        _4thCall.var1 == 1
        _4thCall.var2 == "b"

        producer.hasNext()
        final TestObject _5thCall = producer.next()
        _5thCall.var1 == 3
        _5thCall.var2 == "b"

        producer.hasNext()
        final TestObject _6thCall = producer.next()
        _6thCall.var1 == 5
        _6thCall.var2 == "b"

        producer.hasNext()
        final TestObject _7thCall = producer.next()
        _7thCall.var1 == 1
        _7thCall.var2 == "c"

        producer.hasNext()
        final TestObject _8thCall = producer.next()
        _8thCall.var1 == 3
        _8thCall.var2 == "c"

        producer.hasNext()
        final TestObject _9thCall = producer.next()
        _9thCall.var1 == 5
        _9thCall.var2 == "c"

        !producer.hasNext()
        producer.next()
    }

    def 'given already started iterator when used new iterator should be able to continue old iterator'() {
        given:
        Function<Variables, TestObject> recipe = { var -> new TestObject(var._int("var1").values(1, 3).$()) }
        final Iterator<TestObject> it1 = Producer.from(recipe)
        it1.hasNext()
        it1.next().var1 == 1
        it1.hasNext()

        when:
        final Iterator<TestObject> it2 = Producer.from(recipe)
        it2.hasNext()
        it2.next().var1 == 1
        it2.hasNext()
        it2.next().var1 == 3
        !it2.hasNext()

        then:
        it1.hasNext()
        it1.next().var1 == 3
        !it1.hasNext()
    }

    @FailsWith(NoSuchElementException)
    def 'given 2 variables should produce 12 correct objects'() {
        given:
        final Function<Variables, TestObject> recipe =  {var -> new TestObject(var._int("var1").values(1, 3).$(),
                                                                               var._String("var2").values("a", "b", "c").$(),
                                                                               var._int("var3").values(7, 9).$()) }

        when:
        final Iterator<TestObject> producer = Producer.from(recipe)

        then:
        producer.hasNext()
        final TestObject _1thCall = producer.next()
        _1thCall.var1 == 1
        _1thCall.var2 == "a"
        _1thCall.var3 == 7

        producer.hasNext()
        final TestObject _2thCall = producer.next()
        _2thCall.var1 == 3
        _2thCall.var2 == "a"
        _2thCall.var3 == 7

        producer.hasNext()
        final TestObject _3thCall = producer.next()
        _3thCall.var1 == 1
        _3thCall.var2 == "b"
        _3thCall.var3 == 7

        producer.hasNext()
        final TestObject _4thCall = producer.next()
        _4thCall.var1 == 3
        _4thCall.var2 == "b"
        _4thCall.var3 == 7

        producer.hasNext()
        final TestObject _5thCall = producer.next()
        _5thCall.var1 == 1
        _5thCall.var2 == "c"
        _5thCall.var3 == 7

        producer.hasNext()
        final TestObject _6thCall = producer.next()
        _6thCall.var1 == 3
        _6thCall.var2 == "c"
        _6thCall.var3 == 7

        producer.hasNext()
        final TestObject _7thCall = producer.next()
        _7thCall.var1 == 1
        _7thCall.var2 == "a"
        _7thCall.var3 == 9

        producer.hasNext()
        final TestObject _8thCall = producer.next()
        _8thCall.var1 == 3
        _8thCall.var2 == "a"
        _8thCall.var3 == 9

        producer.hasNext()
        final TestObject _9thCall = producer.next()
        _9thCall.var1 == 1
        _9thCall.var2 == "b"
        _9thCall.var3 == 9

        producer.hasNext()
        final TestObject _10thCall = producer.next()
        _10thCall.var1 == 3
        _10thCall.var2 == "b"
        _10thCall.var3 == 9

        producer.hasNext()
        final TestObject _11thCall = producer.next()
        _11thCall.var1 == 1
        _11thCall.var2 == "c"
        _11thCall.var3 == 9

        producer.hasNext()
        final TestObject _12thCall = producer.next()
        _12thCall.var1 == 3
        _12thCall.var2 == "c"
        _12thCall.var3 == 9

        !producer.hasNext()
        producer.next()
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