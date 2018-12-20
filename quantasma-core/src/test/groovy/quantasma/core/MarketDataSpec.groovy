package quantasma.core

import org.ta4j.core.TimeSeries
import org.ta4j.core.indicators.RSIIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.trading.rules.IsEqualRule
import org.ta4j.core.trading.rules.OverIndicatorRule
import quantasma.core.timeseries.MultipleTimeSeriesBuilder
import quantasma.core.timeseries.TimeSeriesDefinition
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class MarketDataSpec extends Specification {

    private static final ZonedDateTime MIDNIGHT = utc(LocalDateTime.of(2018, 11, 20, 0, 0))
    private static final BarPeriod ONE_MINUTE_PERIOD = BarPeriod.M1

    private static List<ZonedDateTime> 'minutes possibilities from 0:00 to 0:05'() {
        (0..5).collect({ MIDNIGHT.plusMinutes(it) })
    }

    @Unroll
    def 'given 2 time series with max size of 2 bars when add (#barsToAdd) bars first created at time (#time) should have (#expectedBarCount) bars'() {
        given:
        def marketData = createTimeSeriesFor("symbol1", "symbol2")
        def timeSeriesForSymbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD)
        def timeSeriesForSymbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD)

        when:
        barsToAdd.times {
            marketData.add("symbol1", addMinutes(time, it), 1 + it)
        }

        then:
        timeSeriesForSymbol1.getBarCount() == expectedBarCount
        timeSeriesForSymbol2.getBarCount() == expectedBarCount

        where:
        time                    | barsToAdd || expectedBarCount
        MIDNIGHT                | 0         || 0
        MIDNIGHT.plusMinutes(1) | 0         || 0
        MIDNIGHT.plusMinutes(2) | 0         || 0
        MIDNIGHT.plusMinutes(3) | 0         || 0
        MIDNIGHT.plusMinutes(4) | 0         || 0
        MIDNIGHT.plusMinutes(5) | 0         || 0

        MIDNIGHT                | 1         || 1
        MIDNIGHT.plusMinutes(1) | 1         || 1
        MIDNIGHT.plusMinutes(2) | 1         || 1
        MIDNIGHT.plusMinutes(3) | 1         || 1
        MIDNIGHT.plusMinutes(4) | 1         || 1
        MIDNIGHT.plusMinutes(5) | 1         || 1

        MIDNIGHT                | 2         || 2
        MIDNIGHT.plusMinutes(1) | 2         || 2
        MIDNIGHT.plusMinutes(2) | 2         || 2
        MIDNIGHT.plusMinutes(3) | 2         || 2
        MIDNIGHT.plusMinutes(4) | 2         || 2
        MIDNIGHT.plusMinutes(5) | 2         || 2

        MIDNIGHT                | 3         || 2
        MIDNIGHT.plusMinutes(1) | 3         || 2
        MIDNIGHT.plusMinutes(2) | 3         || 2
        MIDNIGHT.plusMinutes(3) | 3         || 2
        MIDNIGHT.plusMinutes(4) | 3         || 2
        MIDNIGHT.plusMinutes(5) | 3         || 2
    }

    @Unroll
    def 'given 2 time series with max size of 2 bars and first bar created at time (#time) when no explicit data insertion for target symbol should take previous value'() {
        given:
        def marketData = createTimeSeriesFor("targetSymbol", "symbol2")
        def targetTimeSeries = marketData.of("targetSymbol").getTimeSeries(ONE_MINUTE_PERIOD)
        def targetClosePriceIndicator = new ClosePriceIndicator(targetTimeSeries)
        def isEqualToOneRule = new IsEqualRule(targetClosePriceIndicator, 1)

        and:
        marketData.add("targetSymbol", time, 0.5)
        assert !isEqualToOneRule.isSatisfied(0)
        marketData.add("targetSymbol", addMinutes(time, 1), 1)
        assert isEqualToOneRule.isSatisfied(1)

        when:
        marketData.add("symbol2", addMinutes(time, 2), 0)

        then:
        isEqualToOneRule.isSatisfied(targetTimeSeries.getEndIndex())

        where:
        time << 'minutes possibilities from 0:00 to 0:05'()
    }

    @Unroll
    def 'given 2 time series with max size of 2 bars and first bar created at time (#time) when no explicit data for target symbol should continue within indicators period'() {
        given:
        def marketData = createTimeSeriesFor("referenceSymbol", "targetSymbol")
        def referenceTimeSeries = marketData.of("referenceSymbol").getTimeSeries(ONE_MINUTE_PERIOD)
        def targetTimeSeries = marketData.of("targetSymbol").getTimeSeries(ONE_MINUTE_PERIOD)
        def rule1 = new OverIndicatorRule(new RSIIndicator(new ClosePriceIndicator(referenceTimeSeries), 2), 70)
        def rule2 = new OverIndicatorRule(new RSIIndicator(new ClosePriceIndicator(targetTimeSeries), 2), 70)

        and:
        marketData.add("referenceSymbol", time, 0)
        marketData.add("targetSymbol", time, 0)
        assert !rule1.isSatisfied(referenceTimeSeries.getEndIndex())
        assert !rule2.isSatisfied(targetTimeSeries.getEndIndex())

        when:
        marketData.add("referenceSymbol", addMinutes(time, 1), 1)

        then:
        rule1.isSatisfied(referenceTimeSeries.getEndIndex())
        !rule2.isSatisfied(targetTimeSeries.getEndIndex())

        and:
        marketData.add("referenceSymbol", addMinutes(time, 2), 0.9)
        marketData.add("targetSymbol", addMinutes(time, 2), 1)
        rule1.isSatisfied(referenceTimeSeries.getEndIndex())
        rule2.isSatisfied(targetTimeSeries.getEndIndex())

        and:
        marketData.add("referenceSymbol", addMinutes(time, 3), 0.8)
        marketData.add("targetSymbol", addMinutes(time, 3), 0.9)
        !rule1.isSatisfied(referenceTimeSeries.getEndIndex())
        rule2.isSatisfied(targetTimeSeries.getEndIndex())

        where:
        time << 'minutes possibilities from 0:00 to 0:05'()
    }

    @Unroll
    def 'when insert unspecified symbol at time (#time) should do nothing and fail silently'() {
        given:
        def marketData = createTimeSeriesFor("knownSymbol")

        when:
        marketData.add("unknownSymbol", time, 1.0)

        then:
        marketData.of('knownSymbol').getMainTimeSeries().getBarCount() == 0

        where:
        time << 'minutes possibilities from 0:00 to 0:05'()
    }

    def 'get unspecified symbol should thrown an illegal argument exception'() {
        given:
        def marketData = createTimeSeriesFor("knownSymbol")

        when:
        marketData.of("unknownSymbol")

        then:
        thrown(IllegalArgumentException)
    }

    private static ZonedDateTime addMinutes(ZonedDateTime time, int minutes) {
        return time.plus(minutes, ChronoUnit.MINUTES)
    }

    private static MarketData createTimeSeriesFor(String... symbols) {
        return new MarketData(
                MultipleTimeSeriesBuilder.basedOn(TimeSeriesDefinition.limited(ONE_MINUTE_PERIOD, 2))
                        .symbols(symbols)
                        .build())
    }

    @Unroll
    def 'given limit of (#timeSeriesLimit) bars add (#barsToAdd) bars to symbol 1 where first bar is created at (#time) should return correct values for symbol 1 and NaNs for symbol 2'() {
        given:
        def marketData = createTwoSymbolMarketData(timeSeriesLimit)
        def m1Symbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD)
        def m1Symbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD)

        when:
        barsToAdd.times {
            marketData.add("symbol1", addMinutes(time, it), it)
        }

        then:
        m1Symbol1.getBarCount() == expectedBarCount

        and:
        barsToAdd.times {
            m1Symbol1.getBar(it).getClosePrice().doubleValue() == expectedValues[it]
        }

        and:
        assertNaNClosedPrices(m1Symbol2, expectedBarCount)

        where:
        time                    | timeSeriesLimit | barsToAdd || expectedBarCount | expectedValues
        MIDNIGHT                | 12              | 12        || 12               | [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        MIDNIGHT.plusMinutes(1) | 12              | 12        || 12               | [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        MIDNIGHT.plusMinutes(2) | 12              | 12        || 12               | [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        MIDNIGHT.plusMinutes(3) | 12              | 12        || 12               | [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        MIDNIGHT.plusMinutes(4) | 12              | 12        || 12               | [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        MIDNIGHT.plusMinutes(5) | 12              | 12        || 12               | [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]

        MIDNIGHT                | 12              | 13        || 12               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
        MIDNIGHT.plusMinutes(1) | 12              | 13        || 12               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
        MIDNIGHT.plusMinutes(2) | 12              | 13        || 12               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
        MIDNIGHT.plusMinutes(3) | 12              | 13        || 12               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
        MIDNIGHT.plusMinutes(4) | 12              | 13        || 12               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
        MIDNIGHT.plusMinutes(5) | 12              | 13        || 12               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]

        MIDNIGHT                | 12              | 17        || 12               | [5, 5, 5, 5, 5, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
        MIDNIGHT.plusMinutes(1) | 12              | 17        || 12               | [5, 5, 5, 5, 5, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
        MIDNIGHT.plusMinutes(2) | 12              | 17        || 12               | [5, 5, 5, 5, 5, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
        MIDNIGHT.plusMinutes(3) | 12              | 17        || 12               | [5, 5, 5, 5, 5, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
        MIDNIGHT.plusMinutes(4) | 12              | 17        || 12               | [5, 5, 5, 5, 5, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
        MIDNIGHT.plusMinutes(5) | 12              | 17        || 12               | [5, 5, 5, 5, 5, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]

        MIDNIGHT                | 11              | 12        || 11               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        MIDNIGHT.plusMinutes(1) | 11              | 12        || 11               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        MIDNIGHT.plusMinutes(2) | 11              | 12        || 11               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        MIDNIGHT.plusMinutes(3) | 11              | 12        || 11               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        MIDNIGHT.plusMinutes(4) | 11              | 12        || 11               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        MIDNIGHT.plusMinutes(5) | 11              | 12        || 11               | [1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]

        MIDNIGHT                | 11              | 13        || 11               | [2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
        MIDNIGHT.plusMinutes(1) | 11              | 13        || 11               | [2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
        MIDNIGHT.plusMinutes(2) | 11              | 13        || 11               | [2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
        MIDNIGHT.plusMinutes(3) | 11              | 13        || 11               | [2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
        MIDNIGHT.plusMinutes(4) | 11              | 13        || 11               | [2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
        MIDNIGHT.plusMinutes(5) | 11              | 13        || 11               | [2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]

        MIDNIGHT                | 11              | 17        || 11               | [6, 6, 6, 6, 6, 6, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
        MIDNIGHT.plusMinutes(1) | 11              | 17        || 11               | [6, 6, 6, 6, 6, 6, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
        MIDNIGHT.plusMinutes(2) | 11              | 17        || 11               | [6, 6, 6, 6, 6, 6, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
        MIDNIGHT.plusMinutes(3) | 11              | 17        || 11               | [6, 6, 6, 6, 6, 6, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
        MIDNIGHT.plusMinutes(4) | 11              | 17        || 11               | [6, 6, 6, 6, 6, 6, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
        MIDNIGHT.plusMinutes(5) | 11              | 17        || 11               | [6, 6, 6, 6, 6, 6, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
    }

    private static void assertNaNClosedPrices(TimeSeries m1TimeSeries, int m1BarsCount) {
        m1TimeSeries.getBarCount() == m1BarsCount
        final int latestIndex = m1TimeSeries.getEndIndex()
        for (int i = 0; i <= latestIndex; i++) {
            m1TimeSeries.getBar(i).getClosePrice().doubleValue() == Double.NaN
        }
    }

    private MarketData createTwoSymbolMarketData(int oneMinutePeriod) {
        return new MarketData(
                MultipleTimeSeriesBuilder.basedOn(TimeSeriesDefinition.limited(ONE_MINUTE_PERIOD, oneMinutePeriod))
                        .symbols("symbol1", "symbol2")
                        .build())
    }

    private static ZonedDateTime utc(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneOffset.UTC)
    }

}