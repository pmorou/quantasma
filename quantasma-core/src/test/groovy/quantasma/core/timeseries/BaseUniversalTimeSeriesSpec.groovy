package quantasma.core.timeseries

import org.ta4j.core.BaseBar
import quantasma.core.BarPeriod
import quantasma.core.timeseries.bar.BaseOneSideBar
import quantasma.core.timeseries.bar.NaNBar
import quantasma.core.timeseries.bar.OneSideBar
import quantasma.core.timeseries.bar.factory.OneSideBarFactory
import spock.lang.Specification

import java.time.Duration
import java.time.ZonedDateTime

class BaseUniversalTimeSeriesSpec extends Specification {

    def time = ZonedDateTime.now()

    def "given time series limited to 2 bars when added 3 bars should return NaN for first one only"() {
        given:
        def timeseries = new BaseUniversalTimeSeries.Builder("symbol", BarPeriod.M1, new OneSideBarFactory()).withMaxBarCount(2).build()

        when:
        3.times {
            timeseries.addBar(bar(timeseries, it))
        }

        then:
        timeseries.getBarCount() + timeseries.getRemovedBarsCount() == 3
        timeseries.getBar(0) == NaNBar.NaN
        timeseries.getBar(1).getClosePrice().doubleValue() == 1
        timeseries.getBar(2).getClosePrice().doubleValue() == 2
    }

    private OneSideBar bar(BaseUniversalTimeSeries timeseries, Integer number) {
        new BaseOneSideBar(new BaseBar(
                Duration.ofMinutes(1),
                time.plusMinutes(number),
                timeseries.numOf(number),
                timeseries.numOf(number),
                timeseries.numOf(number),
                timeseries.numOf(number),
                timeseries.numOf(number),
                timeseries.numOf(number)))
    }
}
