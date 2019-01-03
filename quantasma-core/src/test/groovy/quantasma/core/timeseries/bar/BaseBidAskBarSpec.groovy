package quantasma.core.timeseries.bar

import org.ta4j.core.num.Num
import org.ta4j.core.num.PrecisionNum
import spock.lang.Specification

import java.time.Duration
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.function.Function

class BaseBidAskBarSpec extends Specification {

    def "after adding bid and ask prices 4 times should return correct open-high-low-close values"() {
        given:
        Function<Number, Num> func = { n -> PrecisionNum.valueOf(n) }
        def bar = new BaseBidAskBar(Duration.ofMinutes(1), ZonedDateTime.now(), func)

        when:
        bar.addPrice(func.apply(15), func.apply(25))
        bar.addPrice(func.apply(19), func.apply(29))
        bar.addPrice(func.apply(11), func.apply(21))
        bar.addPrice(func.apply(16), func.apply(26))

        then:
        with(bar) {
            getBidOpenPrice().doubleValue() == 15
            getBidMaxPrice().doubleValue() == 19
            getBidMinPrice().doubleValue() == 11
            getBidClosePrice().doubleValue() == 16
            getAskOpenPrice().doubleValue() == 25
            getAskMaxPrice().doubleValue() == 29
            getAskMinPrice().doubleValue() == 21
            getAskClosePrice().doubleValue() == 26
        }
    }

    private static final ZonedDateTime TIME = LocalDate.of(2019, 1, 3).atStartOfDay().atZone(ZoneOffset.UTC)

    def "build bar from String values"() {
        when:
        def bar = BaseBidAskBar.Builder.create(BarBuilder.create(numFunction()).fromString()).build(
                Duration.ofMinutes(1),
                TIME,
                "1.01",
                "1.01",
                "1.01",
                "1.01",
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
        def bar = BaseBidAskBar.Builder.create(BarBuilder.create(numFunction()).fromDouble()).build(
                Duration.ofMinutes(1),
                TIME,
                1.01d,
                1.01d,
                1.01d,
                1.01d,
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
        def bar = BaseBidAskBar.Builder.create(BarBuilder.create(numFunction()).fromInteger()).build(
                Duration.ofMinutes(1),
                TIME,
                1,
                1,
                1,
                1,
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

    def "build bar from Num values"() {
        when:
        def bar = BaseBidAskBar.Builder.create(BarBuilder.create(numFunction()).fromNum()).build(
                Duration.ofMinutes(1),
                TIME,
                numFunction()(1.01),
                numFunction()(1.01),
                numFunction()(1.01),
                numFunction()(1.01),
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

    private void correctBarValues(BidAskBar targetBar, Double expectedValue) {
        with(targetBar) {
            timePeriod == Duration.ofMinutes(1)
            beginTime != TIME
            endTime != TIME.plusMinutes(1)
            bidOpenPrice.doubleValue() == expectedValue
            bidMaxPrice.doubleValue() == expectedValue
            bidMinPrice.doubleValue() == expectedValue
            bidClosePrice.doubleValue() == expectedValue
            askOpenPrice.doubleValue() == expectedValue
            askMaxPrice.doubleValue() == expectedValue
            askMinPrice.doubleValue() == expectedValue
            askClosePrice.doubleValue() == expectedValue
            volume.doubleValue() == expectedValue
            amount.doubleValue() == expectedValue
        }
    }
}