package quantasma.core

import org.ta4j.core.num.Num
import quantasma.core.timeseries.TimeSeriesDefinition
import quantasma.core.timeseries.bar.BarFactory
import quantasma.core.timeseries.bar.OneSidedBar
import spock.lang.Specification
import spock.lang.Unroll

import java.time.ZonedDateTime
import java.util.function.Function

class MarketDataBuilderSpec extends Specification {

    @Unroll
    def "building failed when base resolution resolution is equal or bigger than aggregated resolution"() {
        when:
        buildMarketData(structureBarPeriod, aggregatedBarPeriod)

        then:
        thrown(IllegalArgumentException)

        where:
        structureBarPeriod | aggregatedBarPeriod
        //======================================
        BarPeriod.M1       | BarPeriod.M1

        BarPeriod.M5       | BarPeriod.M1
        BarPeriod.M5       | BarPeriod.M5

        BarPeriod.M15      | BarPeriod.M1
        BarPeriod.M15      | BarPeriod.M5
        BarPeriod.M15      | BarPeriod.M15

        BarPeriod.M30      | BarPeriod.M1
        BarPeriod.M30      | BarPeriod.M5
        BarPeriod.M30      | BarPeriod.M15
        BarPeriod.M30      | BarPeriod.M30

        BarPeriod.H1       | BarPeriod.M1
        BarPeriod.H1       | BarPeriod.M5
        BarPeriod.H1       | BarPeriod.M15
        BarPeriod.H1       | BarPeriod.M30
        BarPeriod.H1       | BarPeriod.H1

        BarPeriod.H4       | BarPeriod.M1
        BarPeriod.H4       | BarPeriod.M5
        BarPeriod.H4       | BarPeriod.M15
        BarPeriod.H4       | BarPeriod.M30
        BarPeriod.H4       | BarPeriod.H1
        BarPeriod.H4       | BarPeriod.H4

        BarPeriod.D        | BarPeriod.M1
        BarPeriod.D        | BarPeriod.M5
        BarPeriod.D        | BarPeriod.M15
        BarPeriod.D        | BarPeriod.M30
        BarPeriod.D        | BarPeriod.H1
        BarPeriod.D        | BarPeriod.H4
        BarPeriod.D        | BarPeriod.D
    }

    @Unroll
    def "building successful when base resolution resolution is smaller than aggregated resolution"() {
        when:
        buildMarketData(structureBarPeriod, aggregatedBarPeriod)

        then:
        noExceptionThrown()

        where:
        structureBarPeriod | aggregatedBarPeriod
        //======================================
        BarPeriod.M1       | BarPeriod.M5
        BarPeriod.M1       | BarPeriod.M15
        BarPeriod.M1       | BarPeriod.M30
        BarPeriod.M1       | BarPeriod.H1
        BarPeriod.M1       | BarPeriod.H4
        BarPeriod.M1       | BarPeriod.D

        BarPeriod.M5       | BarPeriod.M15
        BarPeriod.M5       | BarPeriod.M30
        BarPeriod.M5       | BarPeriod.H1
        BarPeriod.M5       | BarPeriod.H4
        BarPeriod.M5       | BarPeriod.D

        BarPeriod.M15      | BarPeriod.M30
        BarPeriod.M15      | BarPeriod.H1
        BarPeriod.M15      | BarPeriod.H4
        BarPeriod.M15      | BarPeriod.D

        BarPeriod.M30      | BarPeriod.H1
        BarPeriod.M30      | BarPeriod.H4
        BarPeriod.M30      | BarPeriod.D

        BarPeriod.H1       | BarPeriod.H4
        BarPeriod.H1       | BarPeriod.D

        BarPeriod.H4       | BarPeriod.D
    }

    private MarketData<OneSidedBar> buildMarketData(BarPeriod structureBarPeriod, BarPeriod aggregatedBarPeriod) {
        MarketDataBuilder.basedOn(
                StructureDefinition.model(dummyBarFactory())
                        .resolution(TimeSeriesDefinition.unlimited(structureBarPeriod)))
                .symbols("symbol")
                .aggregate(TimeSeriesDefinition.Group.of("symbol").add(TimeSeriesDefinition.unlimited(aggregatedBarPeriod)))
                .build()
    }

    private static BarFactory<OneSidedBar> dummyBarFactory() {
        new BarFactory<OneSidedBar>() {
            @Override
            OneSidedBar create(BarPeriod barPeriod, Function<Number, Num> numFunction) {
                return null
            }

            @Override
            OneSidedBar create(BarPeriod barPeriod, Function<Number, Num> numFunction, ZonedDateTime time) {
                return null
            }

            @Override
            OneSidedBar getNaNBar() {
                return null
            }
        }
    }
}
