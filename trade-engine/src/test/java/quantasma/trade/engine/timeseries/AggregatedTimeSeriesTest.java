package quantasma.trade.engine.timeseries;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import quantasma.model.CandlePeriod;
import quantasma.trade.engine.DateUtils;
import quantasma.trade.engine.NaNBar;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AggregatedTimeSeriesTest {

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

    public AggregatedTimeSeriesTest(ZonedDateTime time) {
        this.time = time;
    }

    @Test
    public void given1M5And1M1BarsShouldReturnUniqueBarAtIndex0() {
        // given
        final TimeSeries referenceTimeSeries = new BaseTimeSeries.SeriesBuilder().build();
        final AggregatedTimeSeries aggregatedTimeSeries = new AggregatedTimeSeries(referenceTimeSeries, "aggregated", CandlePeriod.M5);
        createM1Bar(0, referenceTimeSeries);
        referenceTimeSeries.addPrice(1);
        createM5Bar(0, aggregatedTimeSeries);
        aggregatedTimeSeries.addPrice(1);

        // when
        final Bar resultAtIndex0 = aggregatedTimeSeries.getBar(0);

        // then
        assertThat(resultAtIndex0.getClosePrice().doubleValue()).isEqualTo(1);
    }

    @Test
    public void given1M5And2M1BarsShouldReturnUniqueBarAtIndex1() {
        // given
        final TimeSeries referenceTimeSeries = new BaseTimeSeries.SeriesBuilder().build();
        final AggregatedTimeSeries aggregatedTimeSeries = new AggregatedTimeSeries(referenceTimeSeries, "aggregated", CandlePeriod.M5);
        for (int i = 0; i < 2; i++) {
            createM1Bar(i, referenceTimeSeries);
            referenceTimeSeries.addPrice(i);
            if (i % 5 == 0) {
                createM5Bar(i, aggregatedTimeSeries);
            }
            aggregatedTimeSeries.addPrice(i);
        }

        // when
        final Bar resultAtIndex0 = aggregatedTimeSeries.getBar(0);
        final Bar resultAtIndex1 = aggregatedTimeSeries.getBar(1);

        // then
        assertThat(resultAtIndex0).isEqualTo(NaNBar.NaN);
        assertThat(resultAtIndex1.getClosePrice().doubleValue()).isEqualTo(1);
    }

    @Test
    public void given2M5BarsShouldReturnUniqueBarsFromIndex1To0() {
        // given
        final TimeSeries referenceTimeSeries = new BaseTimeSeries.SeriesBuilder().build();
        final AggregatedTimeSeries aggregatedTimeSeries = new AggregatedTimeSeries(referenceTimeSeries, "aggregated", CandlePeriod.M5);
        for (int i = 0; i < 6; i++) {
            createM1Bar(i, referenceTimeSeries);
            if (i % 5 == 0) {
                createM5Bar(i, aggregatedTimeSeries);
            }
            referenceTimeSeries.addPrice(i);
            aggregatedTimeSeries.addPrice(i);
        }

        // when
        final Bar resultAtIndex0 = aggregatedTimeSeries.getBar(3);
        final Bar resultAtIndex1 = aggregatedTimeSeries.getBar(3);
        final Bar resultAtIndex2 = aggregatedTimeSeries.getBar(3);
        final Bar resultAtIndex3 = aggregatedTimeSeries.getBar(3);
        final Bar resultAtIndex4 = aggregatedTimeSeries.getBar(4);
        final Bar resultAtIndex5 = aggregatedTimeSeries.getBar(5);

        // then
        assertThat(resultAtIndex0).isEqualTo(NaNBar.NaN);
        assertThat(resultAtIndex1).isEqualTo(NaNBar.NaN);
        assertThat(resultAtIndex2).isEqualTo(NaNBar.NaN);
        assertThat(resultAtIndex3).isEqualTo(NaNBar.NaN);
        assertThat(resultAtIndex4.getClosePrice().doubleValue()).isEqualTo(4);
        assertThat(resultAtIndex5.getClosePrice().doubleValue()).isEqualTo(5);
    }

    private void createM1Bar(int minutesOffset, TimeSeries timeSeries) {
        timeSeries.addBar(new BaseBar(Duration.ofMinutes(1), DateUtils.createEndDate(time.plus(minutesOffset, ChronoUnit.MINUTES), CandlePeriod.M1), timeSeries.function()));
    }

    private void createM5Bar(int minutesOffset, TimeSeries timeSeries) {
        timeSeries.addBar(new BaseBar(Duration.ofMinutes(5), DateUtils.createEndDate(time.plus(minutesOffset, ChronoUnit.MINUTES), CandlePeriod.M5), timeSeries.function()));
    }

    private static ZonedDateTime utc(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
    }
}