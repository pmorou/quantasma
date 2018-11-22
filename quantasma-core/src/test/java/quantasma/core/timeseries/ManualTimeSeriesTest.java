package quantasma.core.timeseries;

import org.junit.Test;
import org.ta4j.core.BaseBar;

import java.time.Duration;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ManualTimeSeriesTest {
    @Test
    public void givenNoBarsShouldReturnBeginIndexAtNeg1AndEndIndexAtNeg1() {
        // given
        final ManualTimeSeries manualTimeSeries = createManualTimeSeries(0);

        // when
        final int beginIndex = manualTimeSeries.getBeginIndex();
        final int endIndex = manualTimeSeries.getEndIndex();

        // then
        assertThat(beginIndex).isEqualTo(-1);
        assertThat(endIndex).isEqualTo(-1);
    }

    @Test
    public void givenNoBarsWhenResetIndexesShouldReturnBeginIndexAtNeg1AndEndIndexAtNeg1() {
        // given
        final ManualTimeSeries manualTimeSeries = createManualTimeSeries(0);

        // when
        manualTimeSeries.resetIndexes();

        // then
        assertThat(manualTimeSeries.getBeginIndex()).isEqualTo(-1);
        assertThat(manualTimeSeries.getEndIndex()).isEqualTo(-1);
    }

    @Test(expected = RuntimeException.class)
    public void givenNoBarsWhenNextIndexShouldThrowAnException() {
        // given
        final ManualTimeSeries manualTimeSeries = createManualTimeSeries(0);

        // when
        manualTimeSeries.nextIndex();
    }

    @Test
    public void given3BarsShouldReturnBeginIndexAt0AndEndIndexAt2() {
        // given
        final ManualTimeSeries manualTimeSeries = createManualTimeSeries(3);

        // when
        final int beginIndex = manualTimeSeries.getBeginIndex();
        final int endIndex = manualTimeSeries.getEndIndex();

        // then
        assertThat(beginIndex).isEqualTo(0);
        assertThat(endIndex).isEqualTo(2);
    }

    @Test
    public void given3BarsWhenResetIndexesShouldReturnBeginIndexAt0AndEndIndexAt0() {
        // given
        final ManualTimeSeries manualTimeSeries = createManualTimeSeries(3);

        // when
        manualTimeSeries.resetIndexes();

        // then
        assertThat(manualTimeSeries.getBeginIndex()).isEqualTo(0);
        assertThat(manualTimeSeries.getEndIndex()).isEqualTo(0);
    }

    @Test
    public void given3BarsAndResetedIndexesWhenNextIndexShouldReturnBeginIndexAt0AndEndIndexAt1() {
        // given
        final ManualTimeSeries manualTimeSeries = createManualTimeSeries(3);
        manualTimeSeries.resetIndexes();

        // when
        manualTimeSeries.nextIndex();

        // then
        assertThat(manualTimeSeries.getBeginIndex()).isEqualTo(0);
        assertThat(manualTimeSeries.getEndIndex()).isEqualTo(1);
    }

    @Test(expected = RuntimeException.class)
    public void given3BarsAndSecondIndexWhenNextIndexShouldReturnThrownAnException() {
        // given
        final ManualTimeSeries manualTimeSeries = createManualTimeSeries(3);

        // when
        manualTimeSeries.nextIndex();
    }

    @Test(expected = RuntimeException.class)
    public void given3BarsAndResetedIndexWhenAddingNewBarShouldReturnThrownAnException() {
        // given
        final int barsCount = 3;
        final ManualTimeSeries manualTimeSeries = createManualTimeSeries(barsCount);
        manualTimeSeries.resetIndexes();
        final BaseBar bar = createBar(ZonedDateTime.now().plusMinutes(barsCount + 1), manualTimeSeries, 0);

        // when
        manualTimeSeries.addBar(bar);
    }

    private static ManualTimeSeries createManualTimeSeries(int barsCount) {
        final ZonedDateTime time = ZonedDateTime.now();
        final ManualTimeSeries manualTimeSeries = new ManualTimeSeries();
        for (int i = 0; i < barsCount; i++) {
            manualTimeSeries.addBar(createBar(time, manualTimeSeries, i));
            manualTimeSeries.addPrice(i);
        }
        return manualTimeSeries;
    }

    private static BaseBar createBar(ZonedDateTime time, ManualTimeSeries manualTimeSeries, int i) {
        return new BaseBar(Duration.ofMinutes(i), time.plusMinutes(i), manualTimeSeries.function());
    }
}