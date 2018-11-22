package quantasma.core.timeseries;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ManualIndexTimeSeriesTest {

    private final static Function<Integer, ManualIndexTimeSeries> useBaseTimeSeries = (barsCount) -> {
        final ZonedDateTime time = ZonedDateTime.now();
        final BaseTimeSeries timeSeries = new BaseTimeSeries();
        for (int i = 0; i < barsCount; i++) {
            timeSeries.addBar(createBar(time, timeSeries, i));
            timeSeries.addPrice(i);
        }
        return new ManualIndexTimeSeries(timeSeries);
    };

    @Parameterized.Parameters(name = "{index}: Using implementation {0}")
    public static Iterable<Object[]> firstBarCreationTimes() {
        return Arrays.asList(new Object[][] {
                {BaseTimeSeries.class, useBaseTimeSeries}
        });
    }

    private final Function<Integer, ManualIndexTimeSeries> manualIndexTimeSeriesFunction;

    public ManualIndexTimeSeriesTest(Class<?> clazz, Function<Integer, ManualIndexTimeSeries> manualIndexTimeSeriesFunction) {
        this.manualIndexTimeSeriesFunction = manualIndexTimeSeriesFunction;
    }

    @Test
    public void givenNoBarsShouldReturnBeginIndexAtNeg1AndEndIndexAtNeg1() {
        // given
        final ManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(0);

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
        final ManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(0);

        // when
        manualTimeSeries.resetIndexes();

        // then
        assertThat(manualTimeSeries.getBeginIndex()).isEqualTo(-1);
        assertThat(manualTimeSeries.getEndIndex()).isEqualTo(-1);
    }

    @Test(expected = RuntimeException.class)
    public void givenNoBarsWhenNextIndexShouldThrowAnException() {
        // given
        final ManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(0);

        // when
        manualTimeSeries.nextIndex();
    }

    @Test
    public void given3BarsShouldReturnBeginIndexAt0AndEndIndexAt2() {
        // given
        final ManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(3);

        // when
        final int beginIndex = manualTimeSeries.getBeginIndex();
        final int endIndex = manualTimeSeries.getEndIndex();

        // then
        assertThat(beginIndex).isEqualTo(0);
        assertThat(endIndex).isEqualTo(2);
    }

    @Test
    public void given3BarsWhenResetIndexesShouldReturnBeginIndexAt0AndEndIndexAtNeg1() {
        // given
        final ManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(3);

        // when
        manualTimeSeries.resetIndexes();

        // then
        assertThat(manualTimeSeries.getBeginIndex()).isEqualTo(0);
        assertThat(manualTimeSeries.getEndIndex()).isEqualTo(-1);
        // in real scenario resetIndexes() is gonna be followed by nextIndex() which will set the value to 0
    }

    @Test
    public void given3BarsAndResetedIndexesWhenNextIndexShouldReturnBeginIndexAt0AndEndIndexAt0() {
        // given
        final ManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(3);
        manualTimeSeries.resetIndexes();

        // when
        manualTimeSeries.nextIndex();

        // then
        assertThat(manualTimeSeries.getBeginIndex()).isEqualTo(0);
        assertThat(manualTimeSeries.getEndIndex()).isEqualTo(0);
    }

    @Test(expected = RuntimeException.class)
    public void given3BarsAndSecondIndexWhenNextIndexShouldReturnThrownAnException() {
        // given
        final ManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(3);

        // when
        manualTimeSeries.nextIndex();
    }

    @Test(expected = RuntimeException.class)
    public void given3BarsAndResetedIndexWhenAddingNewBarShouldReturnThrownAnException() {
        // given
        final int barsCount = 3;
        final ManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(barsCount);
        manualTimeSeries.resetIndexes();
        final BaseBar bar = createBar(ZonedDateTime.now().plusMinutes(barsCount + 1), manualTimeSeries, 0);

        // when
        manualTimeSeries.addBar(bar);
    }


    private ManualIndexTimeSeries createManualTimeSeries(int barsCount) {
        return manualIndexTimeSeriesFunction.apply(barsCount);
    }

    private static BaseBar createBar(ZonedDateTime time, TimeSeries timeSeries, int i) {
        return new BaseBar(Duration.ofMinutes(i), time.plusMinutes(i), timeSeries.function());
    }
}