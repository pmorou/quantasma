package quantasma.core.timeseries

import org.ta4j.core.BaseBar
import org.ta4j.core.TimeSeries
import quantasma.core.BarPeriod
import quantasma.core.DateUtils
import quantasma.core.timeseries.bar.NaNBar
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class BaseAggregatedTimeSeriesSpec extends Specification {

    private static final ZonedDateTime MIDNIGHT = utc(LocalDateTime.of(2018, 11, 20, 0, 0))

    private static List<ZonedDateTime> 'minutes possibilities from 0:00 to 0:05'() {
        (0..5).collect({ MIDNIGHT.plusMinutes(it) })
    }

    @Unroll
    def 'given 1 M5 and 1 M1 bars at time (#time) should return unique bar at index 0'() {
        setup:
        def mainTimeSeries = BaseMainTimeSeries.create(TimeSeriesDefinition.unlimited(BarPeriod.M1), "symbol")
        def aggregatedTimeSeries = createBaseAggregatedTimeSeries(mainTimeSeries)
        createM1Bar(0, mainTimeSeries)
        mainTimeSeries.addPrice(1)
        createM5Bar(0, aggregatedTimeSeries)
        aggregatedTimeSeries.addPrice(1)

        expect:
        aggregatedTimeSeries.getBar(0).getClosePrice().doubleValue() == 1

        where:
        time << 'minutes possibilities from 0:00 to 0:05'()
    }

    @Unroll
    def 'given 1 M5 and 2 M1 bars at time (#time) should return unique bar at index 1'() {
        given:
        def mainTimeSeries = BaseMainTimeSeries.create(TimeSeriesDefinition.unlimited(BarPeriod.M1), "symbol")
        def aggregatedTimeSeries = createBaseAggregatedTimeSeries(mainTimeSeries)
        2.times {
            createM1Bar(it, mainTimeSeries)
            mainTimeSeries.addPrice(it)
            if (it % 5 == 0) {
                createM5Bar(it, aggregatedTimeSeries)
            }
            aggregatedTimeSeries.addPrice(it)
        }

        when:
        def resultAtIndex0 = aggregatedTimeSeries.getBar(0)
        def resultAtIndex1 = aggregatedTimeSeries.getBar(1)

        then:
        resultAtIndex0 == NaNBar.NaN
        resultAtIndex1.getClosePrice().doubleValue() == 1

        where:
        time << 'minutes possibilities from 0:00 to 0:05'()
    }

    @Unroll
    def 'given 2 M5 bars at time (#time) should return unique bars from index 1 to 0'() {
        setup:
        def mainTimeSeries = BaseMainTimeSeries.create(TimeSeriesDefinition.unlimited(BarPeriod.M1), "symbol")
        def aggregatedTimeSeries = createBaseAggregatedTimeSeries(mainTimeSeries)
        6.times {
            createM1Bar(it, mainTimeSeries)
            if (it % 5 == 0) {
                createM5Bar(it, aggregatedTimeSeries)
            }
            mainTimeSeries.addPrice(it)
            aggregatedTimeSeries.addPrice(it)
        }

        expect:
        verifyAll(aggregatedTimeSeries) {
            getBar(0) == NaNBar.NaN
            getBar(1) == NaNBar.NaN
            getBar(2) == NaNBar.NaN
            getBar(3) == NaNBar.NaN
            getBar(4).getClosePrice().doubleValue() == 4
            getBar(5).getClosePrice().doubleValue() == 5
        }

        where:
        time << 'minutes possibilities from 0:00 to 0:05'()
    }

    @Unroll
    def 'given 3 M5 bars should return correct first and last created bar'() {
        setup:
        def mainTimeSeries = BaseMainTimeSeries.create(TimeSeriesDefinition.unlimited(BarPeriod.M1), "symbol")
        def aggregatedTimeSeries = createBaseAggregatedTimeSeries(mainTimeSeries)
        def firstM5Bar = null, secondM5Bar = null, thirdM5Bar = null

        14.times {
            createM1Bar(it, mainTimeSeries)
            if (it == 0) {
                firstM5Bar = createBar(it, aggregatedTimeSeries, BarPeriod.M5)
                aggregatedTimeSeries.addBar(firstM5Bar)
            }
            if (it == 5) {
                secondM5Bar = createBar(it, aggregatedTimeSeries, BarPeriod.M5)
                aggregatedTimeSeries.addBar(secondM5Bar)
            }
            if (it == 10) {
                thirdM5Bar = createBar(it, aggregatedTimeSeries, BarPeriod.M5)
                aggregatedTimeSeries.addBar(thirdM5Bar)
            }
            mainTimeSeries.addPrice(it)
            aggregatedTimeSeries.addPrice(it)
        }

        expect:
        aggregatedTimeSeries.getFirstBar() == firstM5Bar
        aggregatedTimeSeries.getLastBar() == thirdM5Bar

        where:
        time << 'minutes possibilities from 0:00 to 0:05'()
    }

    private static BaseAggregatedTimeSeries createBaseAggregatedTimeSeries(MainTimeSeries mainTimeSeries) {
        return new BaseAggregatedTimeSeries.Builder<?, ?>("symbol", BarPeriod.M5, mainTimeSeries).build()
    }

    private void createM1Bar(int minutesOffset, TimeSeries timeSeries) {
        timeSeries.addBar(createBar(minutesOffset, timeSeries, BarPeriod.M1))
    }

    private void createM5Bar(int minutesOffset, TimeSeries timeSeries) {
        timeSeries.addBar(createBar(minutesOffset, timeSeries, BarPeriod.M5))
    }

    private BaseBar createBar(int minutesOffset, TimeSeries timeSeries, BarPeriod period) {
        return new BaseBar(period.getPeriod(), DateUtils.createEndDate(MIDNIGHT.plus(minutesOffset, ChronoUnit.MINUTES), period), timeSeries.function())
    }

    private static ZonedDateTime utc(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneOffset.UTC)
    }
}