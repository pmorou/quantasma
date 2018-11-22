package quantasma.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.IsEqualRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import quantasma.core.timeseries.MultipleTimeSeriesBuilder;
import quantasma.core.timeseries.TimeSeriesDefinitionImpl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class MarketDataTest {

    private static final ZonedDateTime MIDNIGHT = utc(LocalDateTime.of(2018, 11, 20, 0, 0));

    @Parameterized.Parameters(name = "{index}: First bar created at {0}")
    public static Iterable<Object[]> firstBarCreationTimes() {
        return Arrays.asList(new Object[][] {
                {MIDNIGHT},
                {MIDNIGHT.plusMinutes(1)},
                {MIDNIGHT.plusMinutes(2)},
                {MIDNIGHT.plusMinutes(3)},
                {MIDNIGHT.plusMinutes(4)},
                {MIDNIGHT.plusMinutes(5)}
        });
    }

    private final ZonedDateTime time;

    public MarketDataTest(ZonedDateTime time) {
        this.time = time;
    }

    private static final BarPeriod ONE_MINUTE_PERIOD = BarPeriod.M1;

    @Test
    public void given2TimeSeriesWithMaxSizeOf2ShouldHave0BarsOnStart() {
        // given
        final MarketData marketData = createTimeSeriesFor("symbol1", "symbol2");
        final TimeSeries timeSeriesForSymbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries timeSeriesForSymbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // then
        assertThat(timeSeriesForSymbol1.getBarCount()).isEqualTo(0);
        assertThat(timeSeriesForSymbol2.getBarCount()).isEqualTo(0);
    }

    @Test
    public void given2TimeSeriesWithMaxSizeOf2AddDataToOneOnlyForLengthOf1UnitShouldHave1BarBoth() {
        // given
        final MarketData marketData = createTimeSeriesFor("symbol1", "symbol2");
        final TimeSeries timeSeriesForSymbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries timeSeriesForSymbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        marketData.add("symbol1", time, 1.0);

        // then
        assertThat(timeSeriesForSymbol1.getBarCount()).isEqualTo(1);
        assertThat(timeSeriesForSymbol2.getBarCount()).isEqualTo(1);
    }

    @Test
    public void given2TimeSeriesWithMaxSizeOf2AddDataToOneOnlyForLengthOf2UnitsShouldHave2BarsBoth() {
        // given
        final MarketData marketData = createTimeSeriesFor("symbol1", "symbol2");
        final TimeSeries timeSeriesForSymbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries timeSeriesForSymbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        marketData.add("symbol1", time, 1.0);
        marketData.add("symbol1", addMinutes(time, 1), 2.0);

        // then
        assertThat(timeSeriesForSymbol1.getBarCount()).isEqualTo(2);
        assertThat(timeSeriesForSymbol2.getBarCount()).isEqualTo(2);
    }

    @Test
    public void given2TimeSeriesWithMaxSizeOf2AddDataToOneOnlyForLengthOf3UnitsShouldHave2BarsBoth() {
        // given
        final MarketData marketData = createTimeSeriesFor("symbol1", "symbol2");
        final TimeSeries timeSeriesForSymbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries timeSeriesForSymbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        marketData.add("symbol1", time, 1.0);
        marketData.add("symbol1", addMinutes(time, 1), 2.0);
        marketData.add("symbol1", addMinutes(time, 2), 3.0);

        // then
        assertThat(timeSeriesForSymbol1.getBarCount()).isEqualTo(2);
        assertThat(timeSeriesForSymbol2.getBarCount()).isEqualTo(2);
    }

    @Test
    public void givenNoExplicitDataInsertionForTargetSymbolShouldTakePreviousValue() {
        // given
        final MarketData marketData = createTimeSeriesFor("targetSymbol", "symbol2");
        final TimeSeries targetTimeSeries = marketData.of("targetSymbol").getTimeSeries(ONE_MINUTE_PERIOD);
        final ClosePriceIndicator targetClosePriceIndicator = new ClosePriceIndicator(targetTimeSeries);
        final Rule isEqualToOneRule = new IsEqualRule(targetClosePriceIndicator, 1);

        // when
        marketData.add("targetSymbol", time, 0.5);

        // then
        assertThat(isEqualToOneRule.isSatisfied(targetTimeSeries.getEndIndex())).isFalse();

        // when
        marketData.add("targetSymbol", addMinutes(time, 1), 1);

        // then
        assertThat(isEqualToOneRule.isSatisfied(targetTimeSeries.getEndIndex())).isTrue();

        // when
        marketData.add("symbol2", addMinutes(time, 2), 0);

        // then
        assertThat(isEqualToOneRule.isSatisfied(targetTimeSeries.getEndIndex())).isTrue();
    }

    @Test
    public void givenNoExplicitDataForTargetSymbolShouldContinueWithinIndicatorsPeriod() {
        // given
        final MarketData marketData = createTimeSeriesFor("referenceSymbol", "targetSymbol");
        final TimeSeries referenceTimeSeries = marketData.of("referenceSymbol").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries targetTimeSeries = marketData.of("targetSymbol").getTimeSeries(ONE_MINUTE_PERIOD);
        final ClosePriceIndicator closePrice1 = new ClosePriceIndicator(referenceTimeSeries);
        final ClosePriceIndicator closePrice2 = new ClosePriceIndicator(targetTimeSeries);
        final RSIIndicator rsi1 = new RSIIndicator(closePrice1, 2);
        final RSIIndicator rsi2 = new RSIIndicator(closePrice2, 2);
        final Rule rule1 = new OverIndicatorRule(rsi1, 70);
        final Rule rule2 = new OverIndicatorRule(rsi2, 70);

        // when
        marketData.add("referenceSymbol", time, 0);
        marketData.add("targetSymbol", time, 0);

        // then
        assertThat(rule1.isSatisfied(referenceTimeSeries.getEndIndex())).isFalse();
        assertThat(rule2.isSatisfied(targetTimeSeries.getEndIndex())).isFalse();

        // when
        marketData.add("referenceSymbol", addMinutes(time, 1), 1);

        // then
        assertThat(rule1.isSatisfied(referenceTimeSeries.getEndIndex())).isTrue();
        assertThat(rule2.isSatisfied(targetTimeSeries.getEndIndex())).isFalse();

        // when
        marketData.add("referenceSymbol", addMinutes(time, 2), 0.9);
        marketData.add("targetSymbol", addMinutes(time, 2), 1);

        // then
        assertThat(rule1.isSatisfied(referenceTimeSeries.getEndIndex())).isTrue();
        assertThat(rule2.isSatisfied(targetTimeSeries.getEndIndex())).isTrue();

        // when
        marketData.add("referenceSymbol", addMinutes(time, 3), 0.8);
        marketData.add("targetSymbol", addMinutes(time, 3), 0.9);

        // then
        assertThat(rule1.isSatisfied(referenceTimeSeries.getEndIndex())).isFalse();
        assertThat(rule2.isSatisfied(targetTimeSeries.getEndIndex())).isTrue();
    }

    @Test
    public void insertUnspecifiedSymbolShouldFailSilently() {
        // given
        final MarketData marketData = createTimeSeriesFor("knownSymbol");

        // when
        marketData.add("unknownSymbol", ZonedDateTime.now(), 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUnspecifiedSymbolShouldThrownAnIllegalArgumentException() {
        // given
        final MarketData marketData = createTimeSeriesFor("knownSymbol");

        // when
        marketData.of("unknownSymbol");
    }

    private ZonedDateTime addMinutes(ZonedDateTime time, int minutes) {
        return time.plus(minutes, ChronoUnit.MINUTES);
    }

    private static MarketData createTimeSeriesFor(String... symbols) {
        return new MarketData(
                MultipleTimeSeriesBuilder.basedOn(new TimeSeriesDefinitionImpl(ONE_MINUTE_PERIOD, 2))
                                         .symbols(symbols)
                                         .build());
    }

    @Test
    public void givenLimitOf12BarsAdd12BarsToSymbol1ShouldReturnCorrectValuesForSymbol1AndNaNsForSymbol2() {
        // given
        final MarketData marketData = createTwoSymbolMarketData(12);
        final TimeSeries m1Symbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 12; i++) {
            marketData.add("symbol1", addMinutes(time, i), i);
        }

        // then
        assertThat(m1Symbol1.getBarCount()).isEqualTo(12);
        assertThat(m1Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(0);
        assertThat(m1Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(1);
        assertThat(m1Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(2);
        assertThat(m1Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(3);
        assertThat(m1Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m1Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(5);
        assertThat(m1Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(6);
        assertThat(m1Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(7);
        assertThat(m1Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(8);
        assertThat(m1Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m1Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(10);
        assertThat(m1Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(11);

        assertNaNClosedPrices(m1Symbol2, 12);
    }

    @Test
    public void givenLimitOf12BarsAdd13BarsToSymbol1ShouldReturnCorrectValuesForSymbol1AndNaNsForSymbol2() {
        // given
        final MarketData marketData = createTwoSymbolMarketData(12);
        final TimeSeries m1Symbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 13; i++) {
            marketData.add("symbol1", addMinutes(time, i), i);
        }

        // then
        assertThat(m1Symbol1.getBarCount()).isEqualTo(12);
        assertThat(m1Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(1); // 0-value was dropped, now it points to the current last value value
        assertThat(m1Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(1); // last real value
        assertThat(m1Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(2);
        assertThat(m1Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(3);
        assertThat(m1Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m1Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(5);
        assertThat(m1Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(6);
        assertThat(m1Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(7);
        assertThat(m1Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(8);
        assertThat(m1Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m1Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(10);
        assertThat(m1Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(11);
        assertThat(m1Symbol1.getBar(12).getClosePrice().doubleValue()).isEqualTo(12);

        assertNaNClosedPrices(m1Symbol2, 12);
    }

    @Test
    public void givenLimitOf12BarsAdd17BarsToSymbol1ShouldReturnCorrectValuesForSymbol1AndNaNsForSymbol2() {
        // given
        final MarketData marketData = createTwoSymbolMarketData(12);
        final TimeSeries m1Symbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 17; i++) {
            marketData.add("symbol1", addMinutes(time, i), i);
        }

        // then
        assertThat(m1Symbol1.getBarCount()).isEqualTo(12);
        assertThat(m1Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(5); // =>
        assertThat(m1Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(5);
        assertThat(m1Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(5);
        assertThat(m1Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(5);
        assertThat(m1Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(5); // <= non-existing bars
        assertThat(m1Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(5);
        assertThat(m1Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(6);
        assertThat(m1Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(7);
        assertThat(m1Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(8);
        assertThat(m1Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m1Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(10);
        assertThat(m1Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(11);
        assertThat(m1Symbol1.getBar(12).getClosePrice().doubleValue()).isEqualTo(12);
        assertThat(m1Symbol1.getBar(13).getClosePrice().doubleValue()).isEqualTo(13);
        assertThat(m1Symbol1.getBar(14).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m1Symbol1.getBar(15).getClosePrice().doubleValue()).isEqualTo(15);
        assertThat(m1Symbol1.getBar(16).getClosePrice().doubleValue()).isEqualTo(16);

        assertNaNClosedPrices(m1Symbol2, 12);
    }

    @Test
    public void givenLimitOf11BarsAdd12BarsToSymbol1ShouldReturnCorrectValuesForSymbol1AndNaNsForSymbol2() {
        // given
        final MarketData marketData = createTwoSymbolMarketData(11);
        final TimeSeries m1Symbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 12; i++) {
            marketData.add("symbol1", addMinutes(time, i), i);
        }

        // then
        assertThat(m1Symbol1.getBarCount()).isEqualTo(11);
        assertThat(m1Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(1); // 0-value was dropped, now it points to the current last value value
        assertThat(m1Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(1); // last real value
        assertThat(m1Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(2);
        assertThat(m1Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(3);
        assertThat(m1Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m1Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(5);
        assertThat(m1Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(6);
        assertThat(m1Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(7);
        assertThat(m1Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(8);
        assertThat(m1Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m1Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(10);
        assertThat(m1Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(11);

        assertNaNClosedPrices(m1Symbol2, 11);
    }

    @Test
    public void givenLimitOf11BarsAdd13BarsToSymbol1ShouldReturnCorrectValuesForSymbol1AndNaNsForSymbol2() {
        // given
        final MarketData marketData = createTwoSymbolMarketData(11);
        final TimeSeries m1Symbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 13; i++) {
            marketData.add("symbol1", addMinutes(time, i), i);
        }

        // then
        assertThat(m1Symbol1.getBarCount()).isEqualTo(11);
        assertThat(m1Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(2); // 0-value was dropped, now it points to the current last value value
        assertThat(m1Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(2);
        assertThat(m1Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(2); // last real value
        assertThat(m1Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(3);
        assertThat(m1Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m1Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(5);
        assertThat(m1Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(6);
        assertThat(m1Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(7);
        assertThat(m1Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(8);
        assertThat(m1Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m1Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(10);
        assertThat(m1Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(11);
        assertThat(m1Symbol1.getBar(12).getClosePrice().doubleValue()).isEqualTo(12);

        assertNaNClosedPrices(m1Symbol2, 11);
    }

    @Test
    public void givenLimitOf11BarsAdd17BarsToSymbol1ShouldReturnCorrectValuesForSymbol1AndNaNsForSymbol2() {
        // given
        final MarketData marketData = createTwoSymbolMarketData(11);
        final TimeSeries m1Symbol1 = marketData.of("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = marketData.of("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 17; i++) {
            marketData.add("symbol1", addMinutes(time, i), i);
        }

        // then
        assertThat(m1Symbol1.getBarCount()).isEqualTo(11);
        assertThat(m1Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(6); // 0-value was dropped, now it points to the current last value value
        assertThat(m1Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(6);
        assertThat(m1Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(6);
        assertThat(m1Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(6);
        assertThat(m1Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(6);
        assertThat(m1Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(6);
        assertThat(m1Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(6); // last real value
        assertThat(m1Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(7);
        assertThat(m1Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(8);
        assertThat(m1Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m1Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(10);
        assertThat(m1Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(11);
        assertThat(m1Symbol1.getBar(12).getClosePrice().doubleValue()).isEqualTo(12);
        assertThat(m1Symbol1.getBar(13).getClosePrice().doubleValue()).isEqualTo(13);
        assertThat(m1Symbol1.getBar(14).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m1Symbol1.getBar(15).getClosePrice().doubleValue()).isEqualTo(15);
        assertThat(m1Symbol1.getBar(16).getClosePrice().doubleValue()).isEqualTo(16);

        assertNaNClosedPrices(m1Symbol2, 11);
    }

    private static void assertNaNClosedPrices(TimeSeries m1TimeSeries, int m1BarsCount) {
        assertThat(m1TimeSeries.getBarCount()).isEqualTo(m1BarsCount);
        final int latestIndex = m1TimeSeries.getEndIndex();
        for (int i = 0; i <= latestIndex; i++) {
            assertThat(m1TimeSeries.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }
    }

    private MarketData createTwoSymbolMarketData(int oneMinutePeriod) {
        return new MarketData(
                MultipleTimeSeriesBuilder.basedOn(new TimeSeriesDefinitionImpl(ONE_MINUTE_PERIOD, oneMinutePeriod))
                                         .symbols("symbol1", "symbol2")
                                         .build());
    }

    private static ZonedDateTime utc(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
    }

}