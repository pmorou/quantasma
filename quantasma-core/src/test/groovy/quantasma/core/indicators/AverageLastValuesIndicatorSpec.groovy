package quantasma.core.indicators

import org.ta4j.core.BaseBar
import org.ta4j.core.BaseTimeSeries
import org.ta4j.core.TimeSeries
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.num.NaN
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

class AverageLastValuesIndicatorSpec extends Specification {

    private final static Duration DURATION_OF_1_MIN = Duration.ofMinutes(1)
    private final static ZonedDateTime TIME = ZonedDateTime.of(LocalDateTime.of(2010, 1, 1, 10, 0, 0), ZoneOffset.UTC)

    @Unroll
    def 'given (#lastValuesCount) last values should throw an exception'() {
        when:
        new AverageLastValuesIndicator(new ClosePriceIndicator(new BaseTimeSeries()), lastValuesCount)

        then:
        thrown(ex)

        where:
        lastValuesCount << [0, -1]
        ex = IllegalArgumentException
    }

    @Unroll
    def 'given 3 last values and (#barsCount) values in time series should return (#expectedAverage)'() {
        given:
        def timeSeries = timeSeriesOf(barsCount)
        def averageLastValuesIndicator = new AverageLastValuesIndicator(new ClosePriceIndicator(timeSeries), 3)

        when:
        def result = averageLastValuesIndicator.calculate(timeSeries.getEndIndex())

        then:
        result == expectedAverage

        where:
        barsCount | expectedAverage
        4         | numOf(2)
        3         | numOf(1)
        2         | NaN.NaN
        1         | NaN.NaN
        0         | NaN.NaN
    }

    private def numOf(int number) {
        timeSeriesOf(0).function().apply(number)
    }

    private TimeSeries timeSeriesOf(int valuesCount) {
        TimeSeries timeSeries = new BaseTimeSeries()
        valuesCount.times {
            addOneMinBar(timeSeries, TIME.plusMinutes(it))
            timeSeries.addPrice(it)
        }
        return timeSeries
    }

    private void addOneMinBar(TimeSeries timeSeries, ZonedDateTime endTime) {
        timeSeries.addBar(new BaseBar(DURATION_OF_1_MIN, endTime, timeSeries.function()))
    }
}