package quantasma.trade.engine;

import org.junit.Test;
import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.IsEqualRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import quantasma.model.CandlePeriod;
import quantasma.trade.engine.timeseries.GroupTimeSeriesDefinition;
import quantasma.trade.engine.timeseries.MultipleTimeSeriesBuilder;
import quantasma.trade.engine.timeseries.TimeSeriesDefinitionImpl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseDataServiceTest {

    private static final CandlePeriod ONE_MINUTE_PERIOD = CandlePeriod.M1;
    private static final CandlePeriod FIVE_MINUTE_PERIOD = CandlePeriod.M5;

    @Test
    public void given2TimeSeriesWithMaxSizeOf2ShouldHave0BarsOnStart() {
        // given
        final DataService dataService = createTimeSeriesFor("symbol1", "symbol2");
        final TimeSeries timeSeriesForSymbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries timeSeriesForSymbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // then
        assertThat(timeSeriesForSymbol1.getBarCount()).isEqualTo(0);
        assertThat(timeSeriesForSymbol2.getBarCount()).isEqualTo(0);
    }

    @Test
    public void given2TimeSeriesWithMaxSizeOf2AddDataToOneOnlyForLengthOf1UnitShouldHave1BarBoth() {
        // given
        final ZonedDateTime time = ZonedDateTime.now();
        final DataService dataService = createTimeSeriesFor("symbol1", "symbol2");
        final TimeSeries timeSeriesForSymbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries timeSeriesForSymbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        dataService.add("symbol1", time, 1.0);

        // then
        assertThat(timeSeriesForSymbol1.getBarCount()).isEqualTo(1);
        assertThat(timeSeriesForSymbol2.getBarCount()).isEqualTo(1);
    }

    @Test
    public void given2TimeSeriesWithMaxSizeOf2AddDataToOneOnlyForLengthOf2UnitsShouldHave2BarsBoth() {
        // given
        final ZonedDateTime time = ZonedDateTime.now();
        final DataService dataService = createTimeSeriesFor("symbol1", "symbol2");
        final TimeSeries timeSeriesForSymbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries timeSeriesForSymbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        dataService.add("symbol1", time, 1.0);
        dataService.add("symbol1", addMinutes(time, 1), 2.0);

        // then
        assertThat(timeSeriesForSymbol1.getBarCount()).isEqualTo(2);
        assertThat(timeSeriesForSymbol2.getBarCount()).isEqualTo(2);
    }

    @Test
    public void given2TimeSeriesWithMaxSizeOf2AddDataToOneOnlyForLengthOf3UnitsShouldHave2BarsBoth() {
        // given
        final ZonedDateTime time = ZonedDateTime.now();
        final DataService dataService = createTimeSeriesFor("symbol1", "symbol2");
        final TimeSeries timeSeriesForSymbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries timeSeriesForSymbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);

        // when
        dataService.add("symbol1", time, 1.0);
        dataService.add("symbol1", addMinutes(time, 1), 2.0);
        dataService.add("symbol1", addMinutes(time, 2), 3.0);

        // then
        assertThat(timeSeriesForSymbol1.getBarCount()).isEqualTo(2);
        assertThat(timeSeriesForSymbol2.getBarCount()).isEqualTo(2);
    }

    @Test
    public void givenNoExplicitDataInsertionForTargetSymbolShouldTakePreviousValue() {
        // given
        final ZonedDateTime time = ZonedDateTime.now();
        final DataService dataService = createTimeSeriesFor("targetSymbol", "symbol2");
        final TimeSeries targetTimeSeries = dataService.getMultipleTimeSeries("targetSymbol").getTimeSeries(ONE_MINUTE_PERIOD);
        final ClosePriceIndicator targetClosePriceIndicator = new ClosePriceIndicator(targetTimeSeries);
        final Rule isEqualToOneRule = new IsEqualRule(targetClosePriceIndicator, 1);

        // when
        dataService.add("targetSymbol", time, 0.5);

        // then
        assertThat(isEqualToOneRule.isSatisfied(targetTimeSeries.getEndIndex())).isFalse();

        // when
        dataService.add("targetSymbol", addMinutes(time, 1), 1);

        // then
        assertThat(isEqualToOneRule.isSatisfied(targetTimeSeries.getEndIndex())).isTrue();

        // when
        dataService.add("symbol2", addMinutes(time, 2), 0);

        // then
        assertThat(isEqualToOneRule.isSatisfied(targetTimeSeries.getEndIndex())).isTrue();
    }

    @Test
    public void givenNoExplicitDataForTargetSymbolShouldContinueWithinIndicatorsPeriod() {
        // given
        final ZonedDateTime time = ZonedDateTime.now();
        final DataService dataService = createTimeSeriesFor("referenceSymbol", "targetSymbol");
        final TimeSeries referenceTimeSeries = dataService.getMultipleTimeSeries("referenceSymbol").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries targetTimeSeries = dataService.getMultipleTimeSeries("targetSymbol").getTimeSeries(ONE_MINUTE_PERIOD);
        final ClosePriceIndicator closePrice1 = new ClosePriceIndicator(referenceTimeSeries);
        final ClosePriceIndicator closePrice2 = new ClosePriceIndicator(targetTimeSeries);
        final RSIIndicator rsi1 = new RSIIndicator(closePrice1, 2);
        final RSIIndicator rsi2 = new RSIIndicator(closePrice2, 2);
        final Rule rule1 = new OverIndicatorRule(rsi1, 70);
        final Rule rule2 = new OverIndicatorRule(rsi2, 70);

        // when
        dataService.add("referenceSymbol", time, 0);
        dataService.add("targetSymbol", time, 0);

        // then
        assertThat(rule1.isSatisfied(referenceTimeSeries.getEndIndex())).isFalse();
        assertThat(rule2.isSatisfied(targetTimeSeries.getEndIndex())).isFalse();

        // when
        dataService.add("referenceSymbol", addMinutes(time, 1), 1);

        // then
        assertThat(rule1.isSatisfied(referenceTimeSeries.getEndIndex())).isTrue();
        assertThat(rule2.isSatisfied(targetTimeSeries.getEndIndex())).isFalse();

        // when
        dataService.add("referenceSymbol", addMinutes(time, 2), 0.9);
        dataService.add("targetSymbol", addMinutes(time, 2), 1);

        // then
        assertThat(rule1.isSatisfied(referenceTimeSeries.getEndIndex())).isTrue();
        assertThat(rule2.isSatisfied(targetTimeSeries.getEndIndex())).isTrue();

        // when
        dataService.add("referenceSymbol", addMinutes(time, 3), 0.8);
        dataService.add("targetSymbol", addMinutes(time, 3), 0.9);

        // then
        assertThat(rule1.isSatisfied(referenceTimeSeries.getEndIndex())).isFalse();
        assertThat(rule2.isSatisfied(targetTimeSeries.getEndIndex())).isTrue();
    }

    @Test
    public void insertUnspecifiedSymbolShouldFailSilently() {
        // given
        final DataService dataService = createTimeSeriesFor("knownSymbol");

        // when
        dataService.add("unknownSymbol", ZonedDateTime.now(), 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUnspecifiedSymbolShouldThrownAnIllegalArgumentException() {
        // given
        final DataService dataService = createTimeSeriesFor("knownSymbol");

        // when
        dataService.getMultipleTimeSeries("unknownSymbol");
    }

    private ZonedDateTime addMinutes(ZonedDateTime time, int minutes) {
        return time.plus(minutes, ChronoUnit.MINUTES);
    }

    private static DataService createTimeSeriesFor(String... symbols) {
        return new BaseDataService(
                MultipleTimeSeriesBuilder.basedOn(new TimeSeriesDefinitionImpl(ONE_MINUTE_PERIOD, 2))
                                         .symbols(symbols)
                                         .build());
    }

    @Test
    public void givenAllBarsCreatedAtStartOfHourAndLimitOf12BarsForBothAdd12BarsShouldReturnCorrectBarsOnBothPeriods() {
        // given
        final ZonedDateTime time = utc(LocalDateTime.of(2018, 11, 16, 0, 0));
        final DataService dataService = createTwoSymbolDataService(12, 12);
        final TimeSeries m1Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(FIVE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(FIVE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 12; i++) {
            dataService.add("symbol1", addMinutes(time, i), i);
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
        try {
            assertThat(m1Symbol1.getBar(12).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        assertThat(m5Symbol1.getBarCount()).isEqualTo(3);
        assertThat(m5Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(11);
        assertThat(m5Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(11);
        try {
            assertThat(m5Symbol1.getBar(12).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        // fast check
        assertThat(m1Symbol2.getBarCount()).isEqualTo(12);
        for (int i = 0; i < 12; i++) {
            assertThat(m1Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }

        assertThat(m5Symbol2.getBarCount()).isEqualTo(3);
        for (int i = 0; i < 12; i++) {
            assertThat(m5Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }
    }

    @Test
    public void givenAllBarsCreatedAtStartOfHourAndLimitOf12BarsForBothAdd13BarsShouldReturnCorrectBarsOnBothPeriods() {
        // given
        final ZonedDateTime time = utc(LocalDateTime.of(2018, 11, 16, 0, 0));
        final DataService dataService = createTwoSymbolDataService(12, 12);
        final TimeSeries m1Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(FIVE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(FIVE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 13; i++) {
            dataService.add("symbol1", addMinutes(time, i), i);
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
        try {
            assertThat(m1Symbol1.getBar(13).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        assertThat(m5Symbol1.getBarCount()).isEqualTo(3);
        assertThat(m5Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(12);
        assertThat(m5Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(12);
        assertThat(m5Symbol1.getBar(12).getClosePrice().doubleValue()).isEqualTo(12);
        try {
            assertThat(m5Symbol1.getBar(13).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        // fast check
        assertThat(m1Symbol2.getBarCount()).isEqualTo(12);
        for (int i = 0; i < 13; i++) {
            assertThat(m1Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }

        assertThat(m5Symbol2.getBarCount()).isEqualTo(3);
        for (int i = 0; i < 13; i++) {
            assertThat(m5Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }
    }

    @Test
    public void givenAllBarsCreatedAtStartOfHourAndLimitOf12BarsForBothAdd17BarsWhichCoversOneShiftOf5MinPeriodShouldReturnCorrectBarsOnBothPeriods() {
        // given
        final ZonedDateTime time = utc(LocalDateTime.of(2018, 11, 16, 0, 0));
        final DataService dataService = createTwoSymbolDataService(12, 12);
        final TimeSeries m1Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(FIVE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(FIVE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 17; i++) {
            dataService.add("symbol1", addMinutes(time, i), i);
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
        try {
            assertThat(m1Symbol1.getBar(17).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        assertThat(m5Symbol1.getBarCount()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(9); // =>
        assertThat(m5Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(9); // <= non-existing bars
        assertThat(m5Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m5Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m5Symbol1.getBar(12).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m5Symbol1.getBar(13).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m5Symbol1.getBar(14).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m5Symbol1.getBar(15).getClosePrice().doubleValue()).isEqualTo(16);
        assertThat(m5Symbol1.getBar(16).getClosePrice().doubleValue()).isEqualTo(16);
        try {
            assertThat(m5Symbol1.getBar(17).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        // fast check
        assertThat(m1Symbol2.getBarCount()).isEqualTo(12);
        for (int i = 0; i < 17; i++) {
            assertThat(m1Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }

        assertThat(m5Symbol2.getBarCount()).isEqualTo(4);
        for (int i = 0; i < 17; i++) {
            assertThat(m5Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }
    }

    @Test
    public void givenAllBarsCreatedAtStartOfHourAndLimitOf11And12BarsAdd12BarsShouldReturnCorrectBarsOnBothPeriods() {
        // given
        final ZonedDateTime time = utc(LocalDateTime.of(2018, 11, 16, 0, 0));
        final DataService dataService = createTwoSymbolDataService(11, 12);
        final TimeSeries m1Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(FIVE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(FIVE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 12; i++) {
            dataService.add("symbol1", addMinutes(time, i), i);
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
        try {
            assertThat(m1Symbol1.getBar(12).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        assertThat(m5Symbol1.getBarCount()).isEqualTo(3);
        assertThat(m5Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(11);
        assertThat(m5Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(11);
        try {
            assertThat(m5Symbol1.getBar(12).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        // fast check
        assertThat(m1Symbol2.getBarCount()).isEqualTo(11);
        for (int i = 0; i < 12; i++) {
            assertThat(m1Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }

        assertThat(m5Symbol2.getBarCount()).isEqualTo(3);
        for (int i = 0; i < 12; i++) {
            assertThat(m5Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }
    }

    @Test
    public void givenAllBarsCreatedAtStartOfHourAndLimitOf11And12BarsAdd13BarsShouldReturnCorrectBarsOnBothPeriods() {
        // given
        final ZonedDateTime time = utc(LocalDateTime.of(2018, 11, 16, 0, 0));
        final DataService dataService = createTwoSymbolDataService(11, 12);
        final TimeSeries m1Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(FIVE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(FIVE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 13; i++) {
            dataService.add("symbol1", addMinutes(time, i), i);
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
        try {
            assertThat(m1Symbol1.getBar(13).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        assertThat(m5Symbol1.getBarCount()).isEqualTo(3);
        assertThat(m5Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(12);
        assertThat(m5Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(12);
        assertThat(m5Symbol1.getBar(12).getClosePrice().doubleValue()).isEqualTo(12);
        try {
            assertThat(m5Symbol1.getBar(13).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        // fast check
        assertThat(m1Symbol2.getBarCount()).isEqualTo(11);
        for (int i = 0; i < 13; i++) {
            assertThat(m1Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }

        assertThat(m5Symbol2.getBarCount()).isEqualTo(3);
        for (int i = 0; i < 13; i++) {
            assertThat(m5Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }
    }

    @Test
    public void givenAllBarsCreatedAtStartOfHourAndLimitOf11And12BarsAdd17BarsWhichCoversOneShiftOf5MinPeriodShouldReturnCorrectBarsOnBothPeriods() {
        // given
        final ZonedDateTime time = utc(LocalDateTime.of(2018, 11, 16, 0, 0));
        final DataService dataService = createTwoSymbolDataService(11, 12);
        final TimeSeries m1Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol1 = dataService.getMultipleTimeSeries("symbol1").getTimeSeries(FIVE_MINUTE_PERIOD);
        final TimeSeries m1Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(ONE_MINUTE_PERIOD);
        final TimeSeries m5Symbol2 = dataService.getMultipleTimeSeries("symbol2").getTimeSeries(FIVE_MINUTE_PERIOD);

        // when
        for (int i = 0; i < 17; i++) {
            dataService.add("symbol1", addMinutes(time, i), i);
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
        try {
            assertThat(m1Symbol1.getBar(17).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        assertThat(m5Symbol1.getBarCount()).isEqualTo(4);
        assertThat(m5Symbol1.getBar(0).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(1).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(2).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(3).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(4).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(5).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(6).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(7).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(8).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(9).getClosePrice().doubleValue()).isEqualTo(9);
        assertThat(m5Symbol1.getBar(10).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m5Symbol1.getBar(11).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m5Symbol1.getBar(12).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m5Symbol1.getBar(13).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m5Symbol1.getBar(14).getClosePrice().doubleValue()).isEqualTo(14);
        assertThat(m5Symbol1.getBar(15).getClosePrice().doubleValue()).isEqualTo(16);
        assertThat(m5Symbol1.getBar(16).getClosePrice().doubleValue()).isEqualTo(16);
        try {
            assertThat(m5Symbol1.getBar(17).getClosePrice().doubleValue()).isNull();
            fail();
        } catch (IndexOutOfBoundsException expected) {}

        // fast check
        assertThat(m1Symbol2.getBarCount()).isEqualTo(11);
        for (int i = 0; i < 17; i++) {
            assertThat(m1Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }

        assertThat(m5Symbol2.getBarCount()).isEqualTo(4);
        for (int i = 0; i < 17; i++) {
            assertThat(m5Symbol2.getBar(i).getClosePrice().doubleValue()).isEqualTo(Double.NaN);
        }
    }

    private BaseDataService createTwoSymbolDataService(int oneMinutePeriod, int fiveMinutesPeriod) {
        return new BaseDataService(
                MultipleTimeSeriesBuilder.basedOn(new TimeSeriesDefinitionImpl(ONE_MINUTE_PERIOD, oneMinutePeriod))
                                         .symbols("symbol1", "symbol2")
                                         .aggregate(GroupTimeSeriesDefinition.of("symbol1", "symbol2")
                                                                             .add(new TimeSeriesDefinitionImpl(FIVE_MINUTE_PERIOD, fiveMinutesPeriod)))
                                         .build());
    }

    private static ZonedDateTime utc(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
    }

    private void fail() {
        throw new AssertionError();
    }

}