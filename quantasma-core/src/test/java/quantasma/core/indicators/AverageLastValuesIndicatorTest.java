package quantasma.core.indicators;

import org.junit.Test;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

import java.time.Duration;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AverageLastValuesIndicatorTest {

    private Duration durationOfOneMin = Duration.ofMinutes(1);
    private ZonedDateTime time = ZonedDateTime.now();

    @Test(expected = IllegalArgumentException.class)
    public void givenNegative1LastValuesShouldThrowAnException() {
        new AverageLastValuesIndicator(new ClosePriceIndicator(new BaseTimeSeries()), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void given0LastValuesShouldThrowAnException() {
        new AverageLastValuesIndicator(new ClosePriceIndicator(new BaseTimeSeries()), 0);
    }

    @Test
    public void given3LastValuesAnd4ValuesInTimeSeriesShouldReturn2() {
        // given
        final TimeSeries timeSeries = new BaseTimeSeries();
        addOneMinBar(timeSeries, time);
        timeSeries.addPrice(0);
        addOneMinBar(timeSeries, time.plusMinutes(1));
        timeSeries.addPrice(1);
        addOneMinBar(timeSeries, time.plusMinutes(2));
        timeSeries.addPrice(2);
        addOneMinBar(timeSeries, time.plusMinutes(3));
        timeSeries.addPrice(3);
        final AverageLastValuesIndicator averageLastValuesIndicator = new AverageLastValuesIndicator(new ClosePriceIndicator(timeSeries), 3);

        // when
        final Num result = averageLastValuesIndicator.calculate(timeSeries.getEndIndex());

        // then
        assertThat(result).isEqualTo(timeSeries.numOf(2));
    }

    @Test
    public void given3LastValuesAnd3ValuesInTimeSeriesShouldReturn1() {
        // given
        final TimeSeries timeSeries = new BaseTimeSeries();
        addOneMinBar(timeSeries, time);
        timeSeries.addPrice(0);
        addOneMinBar(timeSeries, time.plusMinutes(1));
        timeSeries.addPrice(1);
        addOneMinBar(timeSeries, time.plusMinutes(2));
        timeSeries.addPrice(2);
        final AverageLastValuesIndicator averageLastValuesIndicator = new AverageLastValuesIndicator(new ClosePriceIndicator(timeSeries), 3);

        // when
        final Num result = averageLastValuesIndicator.calculate(timeSeries.getEndIndex());

        // then
        assertThat(result).isEqualTo(timeSeries.numOf(1));
    }

    @Test
    public void given3LastValuesAnd2ValuesInTimeSeriesShouldReturnNaN() {
        // given
        final TimeSeries timeSeries = new BaseTimeSeries();
        addOneMinBar(timeSeries, time);
        timeSeries.addPrice(0);
        addOneMinBar(timeSeries, time.plusMinutes(1));
        timeSeries.addPrice(1);
        final AverageLastValuesIndicator averageLastValuesIndicator = new AverageLastValuesIndicator(new ClosePriceIndicator(timeSeries), 3);

        // when
        final Num result = averageLastValuesIndicator.calculate(timeSeries.getEndIndex());

        // then
        assertThat(result).isEqualTo(NaN.NaN);
    }

    @Test
    public void given3LastValuesAnd1ValuesInTimeSeriesShouldReturnNaN() {
        // given
        final TimeSeries timeSeries = new BaseTimeSeries();
        addOneMinBar(timeSeries, time);
        timeSeries.addPrice(0);
        final AverageLastValuesIndicator averageLastValuesIndicator = new AverageLastValuesIndicator(new ClosePriceIndicator(timeSeries), 3);

        // when
        final Num result = averageLastValuesIndicator.calculate(timeSeries.getEndIndex());

        // then
        assertThat(result).isEqualTo(NaN.NaN);
    }

    @Test
    public void given3LastValuesAndEmptyTimeSeriesShouldReturnNaN() {
        // given
        final TimeSeries timeSeries = new BaseTimeSeries();
        final AverageLastValuesIndicator averageLastValuesIndicator = new AverageLastValuesIndicator(new ClosePriceIndicator(timeSeries), 3);

        // when
        final Num result = averageLastValuesIndicator.calculate(timeSeries.getEndIndex());

        // then
        assertThat(result).isEqualTo(NaN.NaN);
    }

    private void addOneMinBar(TimeSeries timeSeries, ZonedDateTime endTime) {
        timeSeries.addBar(new BaseBar(durationOfOneMin, endTime, timeSeries.function()));
    }
}