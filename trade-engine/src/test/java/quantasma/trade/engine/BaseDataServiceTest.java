package quantasma.trade.engine;

import org.junit.Test;
import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.IsEqualRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import quantasma.model.CandlePeriod;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseDataServiceTest {

    private static final CandlePeriod ONE_MINUTE_PERIOD = CandlePeriod.M1;

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

}