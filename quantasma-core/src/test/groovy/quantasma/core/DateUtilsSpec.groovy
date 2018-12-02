package quantasma.core

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

class DateUtilsSpec extends Specification {

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

    @Shared
    private def time = ZonedDateTime.of(LocalDateTime.of(2010, 1, 1, 10, 0, 0), ZoneOffset.UTC)

    @Unroll
    def 'given value (#value) and lower bound (#lowerBound)/upper bound (#upperBound):inclusive(#inclusiveUpperBound) should return (#expectedResult)'() {
        when:
        def result = DateUtils.isInRange(value, lowerBound, upperBound, inclusiveUpperBound)

        then:
        result == expectedResult

        where:
        lowerBound           | upperBound          | inclusiveUpperBound || expectedResult
        time                 | time                | true                || true
        time                 | time                | false               || false
        time                 | time.plusSeconds(1) | true                || true
        time                 | time.plusSeconds(1) | false               || true
        time.minusSeconds(1) | time                | true                || true
        time.minusSeconds(1) | time                | false               || false
        time.minusSeconds(1) | time.plusSeconds(1) | true                || true
        time.minusSeconds(1) | time.plusSeconds(1) | false               || true
        time.plusSeconds(1)  | time.plusSeconds(1) | true                || false
        time.plusSeconds(1)  | time.plusSeconds(1) | false               || false
        time.plusSeconds(1)  | time.plusSeconds(2) | true                || false
        time.plusSeconds(1)  | time.plusSeconds(2) | false               || false

        value = time
    }

    @Unroll
    def 'given inner lower bound (#innerLowerBound)-inner upper bound (#innerUpperBound) and outer lower bound (#outerLowerBound)-outer upper bound(#outerUpperBound):inclusive(#inclusiveUpperBound) should return (#expectedResult)'() {
        when:
        def result = DateUtils.isInRange(innerLowerBound, innerUpperBound, outerLowerBound, outerUpperBound, inclusiveUpperBound)

        then:
        result == expectedResult

        where:
        innerLowerBound      | innerUpperBound     | outerLowerBound | outerUpperBound       | inclusiveUpperBound || expectedResult
        time                 | time.plusSeconds(1) | time            | time.plusSeconds(1)   | true                || true
        time                 | time.plusSeconds(1) | time            | time.plusSeconds(1)   | false               || false
        time                 | time.plusSeconds(1) | time            | time.plusSeconds(100) | false               || true
        time.plusSeconds(1)  | time.plusSeconds(2) | time            | time.plusSeconds(100) | false               || true
        time.plusSeconds(1)  | time.plusSeconds(2) | time            | time.plusSeconds(2)   | true                || true
        time.plusSeconds(1)  | time.plusSeconds(2) | time            | time.plusSeconds(2)   | false               || false
        time.minusSeconds(1) | time.plusSeconds(2) | time            | time.plusSeconds(100) | false               || false
    }

    def 'given inner lower bound greater than inner upper bound should throw an exception'() {
        when:
        DateUtils.isInRange(time, time.minusSeconds(1), time, time.plusSeconds(1), true)

        then:
        thrown(IllegalArgumentException)
    }

}