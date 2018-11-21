package quantasma.core.timeseries;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import quantasma.core.BarPeriod;
import quantasma.core.DateUtils;
import quantasma.core.timeseries.bar.NaNBar;

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
        final AggregatedTimeSeries aggregatedTimeSeries = new AggregatedTimeSeries(referenceTimeSeries, "aggregated", BarPeriod.M5);
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
        final AggregatedTimeSeries aggregatedTimeSeries = new AggregatedTimeSeries(referenceTimeSeries, "aggregated", BarPeriod.M5);
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
        final AggregatedTimeSeries aggregatedTimeSeries = new AggregatedTimeSeries(referenceTimeSeries, "aggregated", BarPeriod.M5);
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

    @Test
    public void given3M5BarsShouldReturnCorrectFirstAndLastCreatedBar() {
        // given
        final TimeSeries referenceTimeSeries = new BaseTimeSeries.SeriesBuilder().build();
        final AggregatedTimeSeries aggregatedTimeSeries = new AggregatedTimeSeries(referenceTimeSeries, "aggregated", BarPeriod.M5);
        Bar firstM5Bar = null, secondM5Bar = null, thirdM5Bar = null;

        for (int i = 0; i < 14; i++) {
            createM1Bar(i, referenceTimeSeries);
            if (i == 0) {
                firstM5Bar = createBar(i, aggregatedTimeSeries, BarPeriod.M5);
                aggregatedTimeSeries.addBar(firstM5Bar);
            }
            if (i == 5) {
                secondM5Bar = createBar(i, aggregatedTimeSeries, BarPeriod.M5);
                aggregatedTimeSeries.addBar(secondM5Bar);
            }
            if (i == 10) {
                thirdM5Bar = createBar(i, aggregatedTimeSeries, BarPeriod.M5);
                aggregatedTimeSeries.addBar(thirdM5Bar);
            }
            referenceTimeSeries.addPrice(i);
            aggregatedTimeSeries.addPrice(i);
        }

        // when
        final Bar actualFirstBar = aggregatedTimeSeries.getFirstBar();
        final Bar actualLastBar = aggregatedTimeSeries.getLastBar();

        // then
        assertThat(actualFirstBar).isEqualTo(firstM5Bar);
        assertThat(actualLastBar).isEqualTo(thirdM5Bar);
    }

    private void createM1Bar(int minutesOffset, TimeSeries timeSeries) {
        timeSeries.addBar(createBar(minutesOffset, timeSeries, BarPeriod.M1));
    }

    private void createM5Bar(int minutesOffset, TimeSeries timeSeries) {
        timeSeries.addBar(createBar(minutesOffset, timeSeries, BarPeriod.M5));
    }

    private BaseBar createBar(int minutesOffset, TimeSeries timeSeries, BarPeriod period) {
        return new BaseBar(period.getPeriod(), DateUtils.createEndDate(time.plus(minutesOffset, ChronoUnit.MINUTES), period), timeSeries.function());
    }

    private static ZonedDateTime utc(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
    }
}