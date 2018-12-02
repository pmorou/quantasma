package quantasma.core

import spock.lang.Specification

class IterablesSpec extends Specification {

    def 'given new reusableIterator should end after 3 values'() {
        given:
        def reusableIterator = Iterables.reusableIterator(1, 3, 5)
        def values = new ArrayList<>()

        when:
        while (reusableIterator.hasNext()) {
            values << reusableIterator.next()
        }

        then:
        values.size() == 3
        values == [1, 3, 5]
    }

    def 'given used reusableIterable after reuse should return again 3 values'() {
        given:
        def reusableIterator = Iterables.reusableIterator(1, 3, 5)
        while (reusableIterator.hasNext()) {
            reusableIterator.next()
        }
        def values = []

        when:
        reusableIterator.reuse()

        and:
        while (reusableIterator.hasNext()) {
            values << reusableIterator.next()
        }

        then:
        values.size() == 3
        values == [1, 3, 5]
    }
}