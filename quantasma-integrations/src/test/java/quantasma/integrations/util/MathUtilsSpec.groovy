package quantasma.integrations.util

import spock.lang.Specification
import spock.lang.Unroll

class MathUtilsSpec extends Specification {

    def "given negative decimal point should throw an exception"() {
        when:
        MathUtils.round(1.1234001, -1)

        then:
        thrown(IllegalArgumentException)
    }

    @Unroll
    def "given 1.1234001 rounded to (#decimalPoint) decimal point should return (#result)"() {
        expect:
        MathUtils.round(1.1234001, decimalPoint) == result

        where:
        decimalPoint || result
        0            || 1
        1            || 1.1
        2            || 1.12
        3            || 1.123
        4            || 1.1234
    }
}
