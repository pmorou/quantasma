package quantasma.core

import org.ta4j.core.num.Num
import org.ta4j.core.num.PrecisionNum
import quantasma.core.timeseries.bar.BaseBidAskBar
import spock.lang.Specification

import java.time.Duration
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
        bar.getBidOpenPrice().doubleValue() == 15
        bar.getBidMaxPrice().doubleValue() == 19
        bar.getBidMinPrice().doubleValue() == 11
        bar.getBidClosePrice().doubleValue() == 16
        bar.getAskOpenPrice().doubleValue() == 25
        bar.getAskMaxPrice().doubleValue() == 29
        bar.getAskMinPrice().doubleValue() == 21
        bar.getAskClosePrice().doubleValue() == 26
    }
}