package quantasma.core.analysis.criterion

import org.ta4j.core.BaseBar
import org.ta4j.core.BaseTradingRecord
import org.ta4j.core.Order
import org.ta4j.core.num.DoubleNum
import org.ta4j.core.num.PrecisionNum
import quantasma.core.BarPeriod
import quantasma.core.timeseries.BaseSymbolTimeSeries
import spock.lang.Specification
import spock.lang.Unroll

import java.time.ZonedDateTime

class FinishDepositCriterionSpec extends Specification {

    private final ZonedDateTime time = ZonedDateTime.now()

    @Unroll
    def 'given 1_000 deposit when close 2 trades each (amount) lot should return (#expectedDeposit) deposit'() {
        given:
        def finishDepositCriterion = new FinishDepositCriterion(1000, 0.0001)
        def timeSeries = createTimeSeriesWithBars()

        when:
        def result = new BaseTradingRecord(Order.buyAt(1, timeSeries, timeSeries.numOf(amount)),
                Order.sellAt(2, timeSeries, timeSeries.numOf(amount)),
                Order.buyAt(4, timeSeries, timeSeries.numOf(amount)),
                Order.sellAt(6, timeSeries, timeSeries.numOf(amount)))

        then:
        finishDepositCriterion.calculate(timeSeries, result).delegate == expectedDeposit

        where:
        amount  || expectedDeposit
        100_000 || 1020.0
        10_000  || 1002.0
        20_000  || 1004.0
        10      || 1000.002
    }

    private BaseSymbolTimeSeries createTimeSeriesWithBars() {
        final BaseSymbolTimeSeries timeSeries = new BaseSymbolTimeSeries.Builder("symbol", BarPeriod.M1).build()
        timeSeries.addBar(createM1Bar(0, "1.0000"))
        timeSeries.addBar(createM1Bar(1, "1.0000")) // open #1
        timeSeries.addBar(createM1Bar(2, "1.0001")) // close #1
        timeSeries.addBar(createM1Bar(3, "1.0001"))
        timeSeries.addBar(createM1Bar(4, "1.0001")) // open #2
        timeSeries.addBar(createM1Bar(5, "1.0002"))
        timeSeries.addBar(createM1Bar(6, "1.0002")) // close #2
        timeSeries.addBar(createM1Bar(7, "1.0002"))
        return timeSeries
    }

    private final def numValueOf = { n -> PrecisionNum.valueOf(n) }

    private BaseBar createM1Bar(int rollMinutes, String closePrice) {
        final BaseBar baseBar = new BaseBar(BarPeriod.M1.getPeriod(), time.plusMinutes(rollMinutes), numValueOf)
        baseBar.addPrice(numValueOf(closePrice))
        return baseBar
    }
}