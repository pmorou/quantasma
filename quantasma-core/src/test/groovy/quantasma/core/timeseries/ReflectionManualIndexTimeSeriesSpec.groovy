package quantasma.core.timeseries

import org.ta4j.core.BaseBar
import quantasma.core.BarPeriod
import quantasma.core.timeseries.bar.BaseOneSidedBar
import quantasma.core.timeseries.bar.OneSidedBar
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration
import java.time.ZonedDateTime
import java.util.function.BiFunction

import static quantasma.core.timeseries.ReflectionManualIndexTimeSeriesSpec.ManualIndexTimeSeriesFactory.*
import static quantasma.core.timeseries.ReflectionManualIndexTimeSeriesSpec.createBar

class ReflectionManualIndexTimeSeriesSpec extends Specification {

    @Unroll
    def 'given time series with no bars for bar period (#barPeriod) and implementation (#className) should return beginIndex at -1 and endIndex at -1'() {
        when:
        def manualTimeSeries = createManualTimeSeries(factory, 0, barPeriod)

        then:
        manualTimeSeries.getBeginIndex() == -1
        manualTimeSeries.getEndIndex() == -1

        where:
        className                                | barPeriod    | factory
        BaseUniversalTimeSeries.getName()        | BarPeriod.M1 | BASE_UNIVERSAL_TIME_SERIES
        BaseUniversalTimeSeries.class.getName()  | BarPeriod.M5 | BASE_UNIVERSAL_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M1 | BASE_MAIN_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M5 | BASE_MAIN_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M1 | BASE_AGGREGATED_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M5 | BASE_AGGREGATED_TIME_SERIES
    }

    @Unroll
    def 'given time series with no bars for bar period (#barPeriod) and implementation (#className) when resetIndex() should return beginIndex at -1 and endIndex at -1'() {
        given:
        def manualTimeSeries = createManualTimeSeries(factory, 0, barPeriod)

        when:
        manualTimeSeries.resetIndexes()

        then:
        manualTimeSeries.getBeginIndex() == -1
        manualTimeSeries.getEndIndex() == -1

        where:
        className                                | barPeriod    | factory
        BaseUniversalTimeSeries.getName()        | BarPeriod.M1 | BASE_UNIVERSAL_TIME_SERIES
        BaseUniversalTimeSeries.class.getName()  | BarPeriod.M5 | BASE_UNIVERSAL_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M1 | BASE_MAIN_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M5 | BASE_MAIN_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M1 | BASE_AGGREGATED_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M5 | BASE_AGGREGATED_TIME_SERIES
    }

    @Unroll
    def 'given time series with no bars for bar period (#barPeriod) and implementation (#className) when nextIndex() should throw an exception'() {
        given:
        def manualTimeSeries = createManualTimeSeries(factory, 0, barPeriod)

        when:
        manualTimeSeries.nextIndex()

        then:
        thrown(RuntimeException)

        where:
        className                                | barPeriod    | factory
        BaseUniversalTimeSeries.getName()        | BarPeriod.M1 | BASE_UNIVERSAL_TIME_SERIES
        BaseUniversalTimeSeries.class.getName()  | BarPeriod.M5 | BASE_UNIVERSAL_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M1 | BASE_MAIN_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M5 | BASE_MAIN_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M1 | BASE_AGGREGATED_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M5 | BASE_AGGREGATED_TIME_SERIES
    }

    @Unroll
    def 'given time series with 3 bars for bar period (#barPeriod) and implementation (#className) should return beginIndex at 0 and engIndex at (#expectedEndIndex)'() {
        given:
        def manualTimeSeries = createManualTimeSeries(factory, 3, barPeriod)

        when:
        def beginIndex = manualTimeSeries.getBeginIndex()
        def endIndex = manualTimeSeries.getEndIndex()

        then:
        beginIndex == 0
        endIndex == expectedEndIndex

        where:
        className                                | barPeriod    | factory                     || expectedEndIndex
        BaseUniversalTimeSeries.getName()        | BarPeriod.M1 | BASE_UNIVERSAL_TIME_SERIES  || 2
        BaseUniversalTimeSeries.class.getName()  | BarPeriod.M5 | BASE_UNIVERSAL_TIME_SERIES  || 0
        BaseMainTimeSeries.class.getName()       | BarPeriod.M1 | BASE_MAIN_TIME_SERIES       || 2
        BaseMainTimeSeries.class.getName()       | BarPeriod.M5 | BASE_MAIN_TIME_SERIES       || 0
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M1 | BASE_AGGREGATED_TIME_SERIES || 2
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M5 | BASE_AGGREGATED_TIME_SERIES || 0
    }

    @Unroll
    def 'given time series with 3 bars for bar period (#barPeriod) and implementation (#className) when resetIndexes() should return beginIndex at 0 endIndex at -1'() {
        given:
        def manualTimeSeries = createManualTimeSeries(factory, 3, barPeriod)

        when:
        manualTimeSeries.resetIndexes()

        then:
        manualTimeSeries.getBeginIndex() == 0
        manualTimeSeries.getEndIndex() == -1

        where:
        className                                | barPeriod    | factory
        BaseUniversalTimeSeries.getName()        | BarPeriod.M1 | BASE_UNIVERSAL_TIME_SERIES
        BaseUniversalTimeSeries.class.getName()  | BarPeriod.M5 | BASE_UNIVERSAL_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M1 | BASE_MAIN_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M5 | BASE_MAIN_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M1 | BASE_AGGREGATED_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M5 | BASE_AGGREGATED_TIME_SERIES
    }

    @Unroll
    def 'given time series with 3 bars for bar period (#barPeriod) and implementation (#className) and resetIndexes() when nextIndex() should return beginIndex at 0 and endIndex at 0'() {
        given:
        def manualTimeSeries = createManualTimeSeries(factory, 3, barPeriod)
        manualTimeSeries.resetIndexes()

        when:
        manualTimeSeries.nextIndex()

        then:
        manualTimeSeries.getBeginIndex() == 0
        manualTimeSeries.getEndIndex() == 0

        where:
        className                                | barPeriod    | factory
        BaseUniversalTimeSeries.getName()        | BarPeriod.M1 | BASE_UNIVERSAL_TIME_SERIES
        BaseUniversalTimeSeries.class.getName()  | BarPeriod.M5 | BASE_UNIVERSAL_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M1 | BASE_MAIN_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M5 | BASE_MAIN_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M1 | BASE_AGGREGATED_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M5 | BASE_AGGREGATED_TIME_SERIES
    }

    @Unroll
    def 'given time series with 3 bars and last index for bar period (#barPeriod) and implementation (#className) when nextIndex() should return thrown an exception'() {
        given:
        def manualTimeSeries = createManualTimeSeries(factory, 3, barPeriod)

        when:
        manualTimeSeries.nextIndex()

        then:
        thrown(RuntimeException)

        where:
        className                                | barPeriod    | factory
        BaseUniversalTimeSeries.getName()        | BarPeriod.M1 | BASE_UNIVERSAL_TIME_SERIES
        BaseUniversalTimeSeries.class.getName()  | BarPeriod.M5 | BASE_UNIVERSAL_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M1 | BASE_MAIN_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M5 | BASE_MAIN_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M1 | BASE_AGGREGATED_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M5 | BASE_AGGREGATED_TIME_SERIES
    }

    @Unroll
    def 'given time series with 3 bars for bar period (#barPeriod) and implementation (#className) and resetIndex() when adding new bar should throw an exception'() {
        given:
        def manualTimeSeries = createManualTimeSeries(factory, 3, barPeriod)
        manualTimeSeries.resetIndexes()
        def bar = createBar(manualTimeSeries, 4, Duration.ofMinutes(0)) // any time period

        when:
        manualTimeSeries.addBar(bar)

        then:
        thrown(RuntimeException)

        where:
        className                                | barPeriod    | factory
        BaseUniversalTimeSeries.getName()        | BarPeriod.M1 | BASE_UNIVERSAL_TIME_SERIES
        BaseUniversalTimeSeries.class.getName()  | BarPeriod.M5 | BASE_UNIVERSAL_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M1 | BASE_MAIN_TIME_SERIES
        BaseMainTimeSeries.class.getName()       | BarPeriod.M5 | BASE_MAIN_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M1 | BASE_AGGREGATED_TIME_SERIES
        BaseAggregatedTimeSeries.class.getName() | BarPeriod.M5 | BASE_AGGREGATED_TIME_SERIES
    }

    private static ReflectionManualIndexTimeSeries createManualTimeSeries(factory, barsCount, barPeriod) {
        return factory.function().apply(barsCount, barPeriod)
    }

    static OneSidedBar createBar(UniversalTimeSeries timeSeries, Integer i, Duration timePeriod) {
        new BaseOneSidedBar(new BaseBar(timePeriod, TIME_REF.plusMinutes(i), timeSeries.function()))
    }

    private static final ZonedDateTime TIME_REF = ZonedDateTime.now()

    @FunctionalInterface
    interface ManualIndexTimeSeriesFactory<T extends ManualIndexTimeSeries> {

        BiFunction<Integer, BarPeriod, T> function()

        ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries> BASE_UNIVERSAL_TIME_SERIES = new ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries>() {
            @Override
            BiFunction<Integer, BarPeriod, ReflectionManualIndexTimeSeries> function() {
                { Integer barsCount, BarPeriod barPeriod ->
                    final UniversalTimeSeries timeSeries = new BaseUniversalTimeSeries.Builder("symbol", BarPeriod.M5).build()
                    for (int i = 0; i < barsCount; i++) {
                        if (i % barPeriod.getPeriod().toMinutes() == 0) {
                            timeSeries.addBar(createBar(timeSeries, i, Duration.ofMinutes(i)))
                        }
                        timeSeries.addPrice(i)
                    }
                    ReflectionManualIndexTimeSeries.wrap(timeSeries)
                }
            }
        }

        ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries> BASE_MAIN_TIME_SERIES = new ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries>() {
            @Override
            BiFunction<Integer, BarPeriod, ReflectionManualIndexTimeSeries> function() {
                { barsCount, barPeriod ->
                    final MainTimeSeries timeSeries = new BaseMainTimeSeries.Builder("test", BarPeriod.M5).build()
                    for (int i = 0; i < barsCount; i++) {
                        if (i % barPeriod.getPeriod().toMinutes() == 0) {
                            timeSeries.addBar(createBar(timeSeries, i, Duration.ofMinutes(i)))
                        }
                        timeSeries.addPrice(i)
                    }
                    ReflectionManualIndexTimeSeries.wrap(timeSeries)
                }
            }
        }

        ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries> BASE_AGGREGATED_TIME_SERIES = new ManualIndexTimeSeriesFactory<ReflectionManualIndexTimeSeries>() {
            @Override
            BiFunction<Integer, BarPeriod, ReflectionManualIndexTimeSeries> function() {
                { barsCount, barPeriod ->
                    final MainTimeSeries mainTimeSeries = new BaseMainTimeSeries.Builder("test", BarPeriod.M1).build()
                    final AggregatedTimeSeries aggregatedTimeSeries = new BaseAggregatedTimeSeries.Builder("symbol", BarPeriod.M1, mainTimeSeries).build()
                    for (int i = 0; i < barsCount; i++) {
                        if (i % barPeriod.getPeriod().toMinutes() == 0) {
                            aggregatedTimeSeries.addBar(createBar(aggregatedTimeSeries, i, barPeriod.getPeriod()))
                        }
                        mainTimeSeries.addBar(createBar(mainTimeSeries, i, BarPeriod.M1.getPeriod()))
                        mainTimeSeries.addPrice(i)
                    }
                    ReflectionManualIndexTimeSeries.wrap(aggregatedTimeSeries)
                }
            }
        }

    }
}
