package quantasma.trade.engine;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import quantasma.model.CandlePeriod;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTest {
    @Test
    public void forM1PeriodGivenTruncatedMinutesShouldRoundTo1Minutes() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2010, 1, 1, 10, 0, 0), ZoneOffset.UTC);

        final ZonedDateTime result = DateUtils.createEndDate(zonedDateTime, CandlePeriod.M1);

        assertThat(result.getMinute()).isEqualTo(1);
    }

    @Test
    public void forM1PeriodGiven59MinutesShouldTruncateMinutesAndAdd1Hour() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2010, 1, 1, 10, 59, 0), ZoneOffset.UTC);

        final ZonedDateTime result = DateUtils.createEndDate(zonedDateTime, CandlePeriod.M1);

        assertThat(result.getMinute()).isEqualTo(0);
        assertThat(result.getHour()).isEqualTo(11);
    }

    @Test
    public void forM5PeriodGivenTruncatedMinutesShouldRoundTo5Minutes() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2010, 1, 1, 10, 0, 0), ZoneOffset.UTC);

        final ZonedDateTime result = DateUtils.createEndDate(zonedDateTime, CandlePeriod.M5);

        assertThat(result.getMinute()).isEqualTo(5);
    }

    @Test
    public void forM5PeriodGiven13minutesShouldRoundTo15Minutes() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2010, 1, 1, 10, 13, 0), ZoneOffset.UTC);

        final ZonedDateTime result = DateUtils.createEndDate(zonedDateTime, CandlePeriod.M5);

        assertThat(result.getMinute()).isEqualTo(15);
    }

    @Test
    public void forM5PeriodGiven15minutesShouldRoundTo20Minutes() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2010, 1, 1, 10, 15, 0), ZoneOffset.UTC);

        final ZonedDateTime result = DateUtils.createEndDate(zonedDateTime, CandlePeriod.M5);

        assertThat(result.getMinute()).isEqualTo(20);
    }

    @Test
    public void forM5PeriodGiven16minutesShouldRoundTo20Minutes() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2010, 1, 1, 10, 16, 0), ZoneOffset.UTC);

        final ZonedDateTime result = DateUtils.createEndDate(zonedDateTime, CandlePeriod.M5);

        assertThat(result.getMinute()).isEqualTo(20);
    }

    @Test
    public void forM5PeriodGiven55minutesShouldTruncateMinutesAndAdd1Hour() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2010, 1, 1, 10, 55, 0), ZoneOffset.UTC);

        final ZonedDateTime result = DateUtils.createEndDate(zonedDateTime, CandlePeriod.M5);

        assertThat(result.getMinute()).isEqualTo(0);
        assertThat(result.getHour()).isEqualTo(11);
    }

    @Test
    public void givenValueIsEqualToLowerBoundAndBeforeUpperBoundShouldReturnTrue() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now, now, now.plus(1, ChronoUnit.SECONDS), false);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void givenValueIsAfterLowerBoundAndBeforeUpperBoundShouldReturnTrue() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now, now.minus(1, ChronoUnit.SECONDS), now.plus(1, ChronoUnit.SECONDS), false);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void givenValueIsAfterLowerBoundAndEqualToInclusiveUpperBoundShouldReturnTrue() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now, now.minus(1, ChronoUnit.SECONDS), now, true);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void givenValueIsEqualToLowerBoundAndInclusiveUpperBoundShouldReturnTrue() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now, now, now, true);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void givenValueIsEqualToLowerBoundAndExclusiveUpperBoundShouldReturnFalse() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now, now, now, false);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void givenValueIsBeforeLowerBoundShouldReturnFalse() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now.minus(1, ChronoUnit.SECONDS), now, now, true);

        // then
        assertThat(result).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenInnerLowerBoundIsGreaterThanInnerUpperBoundShouldThrowAnException() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        DateUtils.isInRange(now, now.minus(1, ChronoUnit.SECONDS), now, now.plus(1, ChronoUnit.SECONDS), true);
    }

    @Test
    public void givenInnerBoundIsEqualToOuterLowerAndInclusiveUpperBoundShouldReturnTrue() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now, now.plus(1, ChronoUnit.SECONDS), now, now.plus(1, ChronoUnit.SECONDS), true);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void givenInnerBoundIsEqualToOuterLowerAndExclusiveUpperBoundShouldReturnFalse() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now, now.plus(1, ChronoUnit.SECONDS), now, now.plus(1, ChronoUnit.SECONDS), false);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void givenInnerBoundIsEqualToOuterLowerAndBeforeUpperBoundShouldReturnTrue() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now, now.plus(1, ChronoUnit.SECONDS), now, now.plus(100, ChronoUnit.SECONDS), false);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void givenInnerBoundIsAfterOuterLowerAndBeforeUpperBoundShouldReturnTrue() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now.plus(1, ChronoUnit.SECONDS), now.plus(2, ChronoUnit.SECONDS), now, now.plus(100, ChronoUnit.SECONDS), false);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void givenInnerBoundIsAfterOuterLowerAndEqualToInclusiveUpperBoundShouldReturnTrue() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now.plus(1, ChronoUnit.SECONDS), now.plus(2, ChronoUnit.SECONDS), now, now.plus(2, ChronoUnit.SECONDS), true);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void givenInnerBoundIsAfterOuterLowerAndEqualToExclusiveUpperBoundShouldReturnFalse() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now.plus(1, ChronoUnit.SECONDS), now.plus(2, ChronoUnit.SECONDS), now, now.plus(2, ChronoUnit.SECONDS), false);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void givenInnerBoundIsBeforeOuterLowerBoundShouldReturnFalse() {
        // given
        final ZonedDateTime now = ZonedDateTime.now();

        // when
        final boolean result = DateUtils.isInRange(now.minus(1, ChronoUnit.SECONDS), now.plus(2, ChronoUnit.SECONDS), now, now.plus(100, ChronoUnit.SECONDS), false);

        // then
        assertThat(result).isFalse();
    }
}