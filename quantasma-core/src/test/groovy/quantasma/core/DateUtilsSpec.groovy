package quantasma.core

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

class DateUtilsSpec extends Specification {

    private static final def TEN_O_CLOCK = ZonedDateTime.of(LocalDateTime.of(2010, 1, 1, 10, 0, 0), ZoneOffset.UTC)

    @Unroll
    def 'given bar period (#barPeriod) and time (#hour):(#minutes) should return time (#expectedHour):(#expectedMinutes)'() {
        given:
        def time = createTime(hour, minutes)

        when:
        def result = DateUtils.createEndDate(time, barPeriod)

        then:
        result.getHour() == expectedHour
        result.getMinute() == expectedMinutes

        where:
        hour | minutes | barPeriod    || expectedHour | expectedMinutes
        10   | 00      | BarPeriod.M1 || 10           | 01
        10   | 59      | BarPeriod.M1 || 11           | 00
        10   | 00      | BarPeriod.M5 || 10           | 05
        10   | 13      | BarPeriod.M5 || 10           | 15
        10   | 15      | BarPeriod.M5 || 10           | 20
        10   | 16      | BarPeriod.M5 || 10           | 20
        10   | 55      | BarPeriod.M5 || 11           | 00
    }

    private static ZonedDateTime createTime(Integer hour, Integer minutes) {
        ZonedDateTime.of(LocalDateTime.of(2010, 1, 1, hour, minutes, 0), ZoneOffset.UTC)
    }

    @Unroll
    def 'given value (#value) and lower bound (#lowerBound)/upper bound (#upperBound):inclusive(#inclusiveUpperBound) should return (#expectedResult)'() {
        when:
        def result = DateUtils.isInRange(value, lowerBound, upperBound, inclusiveUpperBound)

        then:
        result == expectedResult

        where:
        lowerBound                  | upperBound                 | inclusiveUpperBound || expectedResult
        TEN_O_CLOCK                 | TEN_O_CLOCK                | true                || true
        TEN_O_CLOCK                 | TEN_O_CLOCK                | false               || false
        TEN_O_CLOCK                 | TEN_O_CLOCK.plusSeconds(1) | true                || true
        TEN_O_CLOCK                 | TEN_O_CLOCK.plusSeconds(1) | false               || true
        TEN_O_CLOCK.minusSeconds(1) | TEN_O_CLOCK                | true                || true
        TEN_O_CLOCK.minusSeconds(1) | TEN_O_CLOCK                | false               || false
        TEN_O_CLOCK.minusSeconds(1) | TEN_O_CLOCK.plusSeconds(1) | true                || true
        TEN_O_CLOCK.minusSeconds(1) | TEN_O_CLOCK.plusSeconds(1) | false               || true
        TEN_O_CLOCK.plusSeconds(1)  | TEN_O_CLOCK.plusSeconds(1) | true                || false
        TEN_O_CLOCK.plusSeconds(1)  | TEN_O_CLOCK.plusSeconds(1) | false               || false
        TEN_O_CLOCK.plusSeconds(1)  | TEN_O_CLOCK.plusSeconds(2) | true                || false
        TEN_O_CLOCK.plusSeconds(1)  | TEN_O_CLOCK.plusSeconds(2) | false               || false

        value = TEN_O_CLOCK
    }

    @Unroll
    def 'given inner lower bound (#innerLowerBound)-inner upper bound (#innerUpperBound) and outer lower bound (#outerLowerBound)-outer upper bound(#outerUpperBound):inclusive(#inclusiveUpperBound) should return (#expectedResult)'() {
        when:
        def result = DateUtils.isInRange(innerLowerBound, innerUpperBound, outerLowerBound, outerUpperBound, inclusiveUpperBound)

        then:
        result == expectedResult

        where:
        innerLowerBound             | innerUpperBound            | outerLowerBound | outerUpperBound              | inclusiveUpperBound || expectedResult
        TEN_O_CLOCK                 | TEN_O_CLOCK.plusSeconds(1) | TEN_O_CLOCK     | TEN_O_CLOCK.plusSeconds(1)   | true                || true
        TEN_O_CLOCK                 | TEN_O_CLOCK.plusSeconds(1) | TEN_O_CLOCK     | TEN_O_CLOCK.plusSeconds(1)   | false               || false
        TEN_O_CLOCK                 | TEN_O_CLOCK.plusSeconds(1) | TEN_O_CLOCK     | TEN_O_CLOCK.plusSeconds(100) | false               || true
        TEN_O_CLOCK.plusSeconds(1)  | TEN_O_CLOCK.plusSeconds(2) | TEN_O_CLOCK     | TEN_O_CLOCK.plusSeconds(100) | false               || true
        TEN_O_CLOCK.plusSeconds(1)  | TEN_O_CLOCK.plusSeconds(2) | TEN_O_CLOCK     | TEN_O_CLOCK.plusSeconds(2)   | true                || true
        TEN_O_CLOCK.plusSeconds(1)  | TEN_O_CLOCK.plusSeconds(2) | TEN_O_CLOCK     | TEN_O_CLOCK.plusSeconds(2)   | false               || false
        TEN_O_CLOCK.minusSeconds(1) | TEN_O_CLOCK.plusSeconds(2) | TEN_O_CLOCK     | TEN_O_CLOCK.plusSeconds(100) | false               || false
    }

    def 'given inner lower bound greater than inner upper bound should throw an exception'() {
        when:
        DateUtils.isInRange(TEN_O_CLOCK, TEN_O_CLOCK.minusSeconds(1), TEN_O_CLOCK, TEN_O_CLOCK.plusSeconds(1), true)

        then:
        thrown(IllegalArgumentException)
    }

}