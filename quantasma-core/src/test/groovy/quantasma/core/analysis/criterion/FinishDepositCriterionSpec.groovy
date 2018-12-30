package quantasma.core.analysis.criterion

import org.ta4j.core.BaseBar
import org.ta4j.core.BaseTradingRecord
import org.ta4j.core.Order
import org.ta4j.core.num.PrecisionNum
import quantasma.core.BarPeriod
import quantasma.core.timeseries.BaseUniversalTimeSeries
import quantasma.core.timeseries.UniversalTimeSeries
import quantasma.core.timeseries.bar.BaseOneSidedBar
import quantasma.core.timeseries.bar.OneSidedBar
import quantasma.core.timeseries.bar.factory.OneSidedBarFactory
import spock.lang.Specification
import spock.lang.Unroll

import java.time.ZonedDateTime
import java.util.function.Function

class FinishDepositCriterionSpec extends Specification {

    private static final ZonedDateTime TIME = ZonedDateTime.now()
    private static final Function<Number, PrecisionNum> NUM_FUNC = { n -> PrecisionNum.valueOf(n) }

    @Unroll
    def 'given 1_000 deposit when close 2 trades each (amount) lot should return (#expectedDeposit) deposit'() {
        given:
        def finishDepositCriterion = new FinishDepositCriterion(1000, 0.0001)
        def timeSeries = createTimeSeriesWithBars().timeSeries()

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

    private static BaseUniversalTimeSeries createTimeSeriesWithBars() {
        final BaseUniversalTimeSeries timeSeries = new BaseUniversalTimeSeries.Builder("symbol", BarPeriod.M1, new OneSidedBarFactory())
                .withNumTypeOf(NUM_FUNC)
                .build()

        addM1Bar(0, "1.0000", timeSeries)
        addM1Bar(1, "1.0000", timeSeries) // open #1
        addM1Bar(2, "1.0001", timeSeries) // close #1
        addM1Bar(3, "1.0001", timeSeries)
        addM1Bar(4, "1.0001", timeSeries) // open #2
        addM1Bar(5, "1.0002", timeSeries)
        addM1Bar(6, "1.0002", timeSeries) // close #2
        addM1Bar(7, "1.0002", timeSeries)
        return timeSeries
    }

    private static void addM1Bar(int rollMinutes, String closePrice, UniversalTimeSeries timeSeries) {
        final OneSidedBar bar = new BaseOneSidedBar(new BaseBar(BarPeriod.M1.getPeriod(), TIME.plusMinutes(rollMinutes), NUM_FUNC))
        bar.addPrice(NUM_FUNC.apply(new BigDecimal(closePrice)))
        timeSeries.addBar(bar)
    }

}