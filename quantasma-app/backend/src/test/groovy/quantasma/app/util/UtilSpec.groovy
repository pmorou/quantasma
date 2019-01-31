package quantasma.app.util

import spock.lang.Specification
import spock.lang.Unroll

class UtilSpec extends Specification {

    def 'should fail when round to -1 place'() {
        when:
        Util.round(2.12345, -1)

        then:
        thrown(RuntimeException)
    }

    @Unroll
    def 'should round 2.12345 to (#place) place'() {
        expect:
        Util.round(2.12345, place) == expectedResult

        where:
        place || expectedResult
        0     || 2.0
        2     || 2.12
    }

    @Unroll
    def 'should round percentage (#value) to (#place)'() {
        expect:
        Util.toPercentage(value, place) == expectedResult

        where:
        value   | place || expectedResult
        2.12345 | 0     || 212.0
        2.12345 | 1     || 212.3
        2.12345 | 2     || 212.35
        0.12345 | 0     || 12.0
        0.12345 | 1     || 12.3
        0.12345 | 2     || 12.35
    }

}
