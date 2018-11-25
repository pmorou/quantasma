package quantasma.core.timeseries;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import quantasma.core.BarPeriod;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ReflectionManualIndexTimeSeriesTest {

    @Parameterized.Parameters(name = "{index} - bar period: {1}, implementation: {0}")
    public static Iterable<Object[]> firstBarCreationTimes() {
        return Arrays.asList(new Object[][] {
                {BaseTimeSeries.class.getName(), BarPeriod.M1, ManualIndexTimeSeriesFactory.BASE_TIME_SERIES, 2},
                {BaseSymbolTimeSeries.class.getName(), BarPeriod.M1, ManualIndexTimeSeriesFactory.BASE_SYMBOL_TIME_SERIES, 2},
                {BaseSymbolTimeSeries.class.getName(), BarPeriod.M5, ManualIndexTimeSeriesFactory.BASE_SYMBOL_TIME_SERIES, 0},
                {BaseMainTimeSeries.class.getName(), BarPeriod.M1, ManualIndexTimeSeriesFactory.BASE_MAIN_TIME_SERIES, 2},
                {BaseMainTimeSeries.class.getName(), BarPeriod.M5, ManualIndexTimeSeriesFactory.BASE_MAIN_TIME_SERIES, 0},
                {BaseAggregatedTimeSeries.class.getName(), BarPeriod.M1, ManualIndexTimeSeriesFactory.BASE_AGGREGATED_TIME_SERIES, 2},
                {BaseAggregatedTimeSeries.class.getName(), BarPeriod.M5, ManualIndexTimeSeriesFactory.BASE_AGGREGATED_TIME_SERIES, 0}
        });
    }

    private static BarPeriod givenPeriod = null;

    private final ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries> factory;
    private final int expectedBarsCountAfter3Mins;

    public ReflectionManualIndexTimeSeriesTest(String classNameMessageOnly,
                                               BarPeriod givenPeriod,
                                               ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries> factory,
                                               int expectedBarsCountAfter3Mins) {
        this.factory = factory;
        this.expectedBarsCountAfter3Mins = expectedBarsCountAfter3Mins;

        // workaround
        ReflectionManualIndexTimeSeriesTest.givenPeriod = givenPeriod;
    }

    @Test
    public void givenNoBarsShouldReturnBeginIndexAtNeg1AndEndIndexAtNeg1() {
        // given
        final ReflectionManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(0);

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
        final ReflectionManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(0);

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
        final ReflectionManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(3);

        // when
        final int beginIndex = manualTimeSeries.getBeginIndex();
        final int endIndex = manualTimeSeries.getEndIndex();

        // then
        assertThat(beginIndex).isEqualTo(0);
        assertThat(endIndex).isEqualTo(expectedBarsCountAfter3Mins);
    }

    @Test
    public void given3BarsWhenResetIndexesShouldReturnBeginIndexAt0AndEndIndexAtNeg1() {
        // given
        final ReflectionManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(3);

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
        final ReflectionManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(3);
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
        final ReflectionManualIndexTimeSeries manualTimeSeries = createManualTimeSeries(barsCount);
        manualTimeSeries.resetIndexes();
        final BaseBar bar = createBar(manualTimeSeries, barsCount + 1, Duration.ofMinutes(0));

        // when
        manualTimeSeries.addBar(bar);
    }


    private ReflectionManualIndexTimeSeries createManualTimeSeries(int barsCount) {
        return factory.create(barsCount);
    }

    private static BaseBar createBar(TimeSeries timeSeries, int i, Duration timePeriod) {
        return new BaseBar(timePeriod, TIME_REF.plusMinutes(i), timeSeries.function());
    }

    private static final ZonedDateTime TIME_REF = ZonedDateTime.now();

    private static BaseMainTimeSeries createMainTimeSeries(BarPeriod barPeriod) {
        return new BaseMainTimeSeries("test", "test", barPeriod);
    }

    @FunctionalInterface
    private interface ManualIndexTimeSeriesFactory<T extends ManualIndexTimeSeries> {


        Function<Integer, T> function();

        default T create(int barsCount) {
            return function().apply(barsCount);
        }

        static BarPeriod getBarPeriod() {
            return ReflectionManualIndexTimeSeriesTest.givenPeriod;
        }

        ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries> BASE_TIME_SERIES = () ->
                (Integer barsCount) -> {
                    final TimeSeries timeSeries = new BaseTimeSeries();
                    for (int i = 0; i < barsCount; i++) {
                        timeSeries.addBar(createBar(timeSeries, i, Duration.ofMinutes(i)));
                        timeSeries.addPrice(i);
                    }
                    return ReflectionManualIndexTimeSeries.wrap(timeSeries);
                };

        ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries> BASE_SYMBOL_TIME_SERIES = () ->
                (Integer barsCount) -> {
                    final BarPeriod barPeriod = getBarPeriod();
                    final SymbolTimeSeries timeSeries = new BaseSymbolTimeSeries("test", "test", BarPeriod.M5);
                    for (int i = 0; i < barsCount; i++) {
                        if (i % barPeriod.getPeriod().toMinutes() == 0) {
                            timeSeries.addBar(createBar(timeSeries, i, Duration.ofMinutes(i)));
                        }
                        timeSeries.addPrice(i);
                    }
                    return ReflectionManualIndexTimeSeries.wrap(timeSeries);
                };

        ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries> BASE_MAIN_TIME_SERIES = () ->
                (Integer barsCount) -> {
                    final BarPeriod barPeriod = getBarPeriod();
                    final MainTimeSeries timeSeries = createMainTimeSeries(BarPeriod.M5);
                    for (int i = 0; i < barsCount; i++) {
                        if (i % barPeriod.getPeriod().toMinutes() == 0) {
                            timeSeries.addBar(createBar(timeSeries, i, Duration.ofMinutes(i)));
                        }
                        timeSeries.addPrice(i);
                    }
                    return ReflectionManualIndexTimeSeries.wrap(timeSeries);
                };
        ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries> BASE_AGGREGATED_TIME_SERIES = () ->
                (Integer barsCount) -> {
                    final BarPeriod barPeriod = getBarPeriod();
                    final MainTimeSeries mainTimeSeries = createMainTimeSeries(BarPeriod.M1);
                    final AggregatedTimeSeries aggregatedTimeSeries = new BaseAggregatedTimeSeries(mainTimeSeries, "test", "symbol", BarPeriod.M1);
                    for (int i = 0; i < barsCount; i++) {
                        if (i % barPeriod.getPeriod().toMinutes() == 0) {
                            aggregatedTimeSeries.addBar(createBar(aggregatedTimeSeries, i, barPeriod.getPeriod()));
                        }
                        mainTimeSeries.addBar(createBar(mainTimeSeries, i, BarPeriod.M1.getPeriod()));
                        mainTimeSeries.addPrice(i);
                    }
                    return ReflectionManualIndexTimeSeries.wrap(aggregatedTimeSeries);
                };

    }
}