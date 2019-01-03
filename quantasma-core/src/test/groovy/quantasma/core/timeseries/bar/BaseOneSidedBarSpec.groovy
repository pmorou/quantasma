package quantasma.core.timeseries.bar

import org.ta4j.core.num.PrecisionNum
import quantasma.core.timeseries.bar.generic.Argument
import spock.lang.Specification

import java.time.Duration
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime

class BaseOneSidedBarSpec extends Specification {

    private static final ZonedDateTime TIME = LocalDate.of(2019, 1, 3).atStartOfDay().atZone(ZoneOffset.UTC)

    def "build bar from String values"() {
        when:
        def bar = BaseOneSidedBar.GenericConstructor.from(numFunction(), Argument.ofString()).create(
                Duration.ofMinutes(1),
                TIME,
                "1.01",
                "1.01",
                "1.01",
                "1.01",
                "1.01",
                "1.01")

        then:
        noExceptionThrown()
        correctBarValues(bar, 1.01d)
    }

    def "build bar from Double values"() {
        when:
        def bar = BaseOneSidedBar.GenericConstructor.from(numFunction(), Argument.ofDouble()).create(
                Duration.ofMinutes(1),
                TIME,
                1.01d,
                1.01d,
                1.01d,
                1.01d,
                1.01d,
                1.01d)

        then:
        noExceptionThrown()
        correctBarValues(bar, 1.01d)
    }

    def "build bar from Integer values"() {
        when:
        def bar = BaseOneSidedBar.GenericConstructor.from(numFunction(), Argument.ofInteger()).create(
                Duration.ofMinutes(1),
                TIME,
                1,
                1,
                1,
                1,
                1,
                1)

        then:
        noExceptionThrown()
        correctBarValues(bar, 1.00d)
    }

    def "build bar from Long values"() {
        when:
        def bar = BaseOneSidedBar.GenericConstructor.from(numFunction(), Argument.of(Long)).create(
                Duration.ofMinutes(1),
                TIME,
                1L,
                1L,
                1L,
                1L,
                1L,
                1L)

        then:
        noExceptionThrown()
        correctBarValues(bar, 1.00d)
    }

    def "build bar from Custom object values"() {
        when:
        def bar = BaseOneSidedBar.GenericConstructor.from(numFunction(), Argument.of({ CustomObject obj -> obj.value })).create(
                Duration.ofMinutes(1),
                TIME,
                new CustomObject(value: 1.01d),
                new CustomObject(value: 1.01d),
                new CustomObject(value: 1.01d),
                new CustomObject(value: 1.01d),
                new CustomObject(value: 1.01d),
                new CustomObject(value: 1.01d))

        then:
        noExceptionThrown()
        correctBarValues(bar, 1.01d)
    }

    def "initialize bar from Num values"() {
        when:
        def bar = new BaseOneSidedBar(
                Duration.ofMinutes(1),
                TIME,
                numFunction()(1.01),
                numFunction()(1.01),
                numFunction()(1.01),
                numFunction()(1.01),
                numFunction()(1.01),
                numFunction()(1.01))

        then:
        noExceptionThrown()
        correctBarValues(bar, 1.01d)
    }

    private static Closure<PrecisionNum> numFunction() {
        { n -> PrecisionNum.valueOf(n) }
    }

    private void correctBarValues(OneSidedBar targetBar, Double expectedValue) {
        with(targetBar) {
            timePeriod == Duration.ofMinutes(1)
            beginTime != TIME
            endTime != TIME.plusMinutes(1)
            openPrice.doubleValue() == expectedValue
            maxPrice.doubleValue() == expectedValue
            minPrice.doubleValue() == expectedValue
            closePrice.doubleValue() == expectedValue
            volume.doubleValue() == expectedValue
            amount.doubleValue() == expectedValue
        }
    }

    class CustomObject {
        double value
    }
}
