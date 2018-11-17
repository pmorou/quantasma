package quantasma.trade.engine;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import quantasma.model.CandlePeriod;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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
}