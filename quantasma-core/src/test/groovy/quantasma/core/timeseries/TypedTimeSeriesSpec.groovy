package quantasma.core.timeseries

import org.ta4j.core.Bar
import org.ta4j.core.BaseTimeSeries
import org.ta4j.core.num.Num
import spock.lang.Specification

import java.time.Duration
import java.time.ZonedDateTime

class TypedTimeSeriesSpec extends Specification {

    def 'given time series typed as SuperBar when adding SuperBar-non-implementing-class should throw an exception'() {
        given:
        TypedTimeSeries<SuperBar> typedTimeSeries = TypedTimeSeries.create(SuperBar.class, new BaseTimeSeries())

        when:
        typedTimeSeries.addBar((SuperBar) new DifferentBarClass()) // forcing cast

        then:
        thrown(ClassCastException)
    }

    def 'given time series typed as SuperBar when adding SuperBar-implementing-class should return 1 as bar count'() {
        given:
        TypedTimeSeries<SuperBar> typedTimeSeries = TypedTimeSeries.create(SuperBar.class, new BaseTimeSeries())

        when:
        typedTimeSeries.addBar(new SuperBarImpl())

        then:
        typedTimeSeries.getTimeSeries().getBarCount() == 1
    }

    def 'given time series typed as SuperBar when adding 1 SuperBar-implementing-class should return it as first and last bar'() {
        given:
        TypedTimeSeries<SuperBar> typedTimeSeries = TypedTimeSeries.create(SuperBar.class, new BaseTimeSeries())
        def expectedFirstAndLastBar = new SuperBarImpl()

        when:
        typedTimeSeries.addBar(expectedFirstAndLastBar)

        then:
        typedTimeSeries.getFirstBar() == expectedFirstAndLastBar
        typedTimeSeries.getLastBar() == expectedFirstAndLastBar
    }

    void 'given time series typed as SuperBar when adding 2 SuperBar-implementing-classes should return them as first and last bar'() {
        given:
        TypedTimeSeries<SuperBar> typedTimeSeries = TypedTimeSeries.create(SuperBar.class, new BaseTimeSeries())
        def firstBar = new SuperBarImpl(ZonedDateTime.now())
        def lastBar = new SuperBarImpl(ZonedDateTime.now().plusDays(2))

        when:
        typedTimeSeries.addBar(firstBar)
        typedTimeSeries.addBar(lastBar)

        then:
        typedTimeSeries.getTimeSeries().getBarCount() == 2
        typedTimeSeries.getFirstBar() == firstBar
        typedTimeSeries.getLastBar() != firstBar
        typedTimeSeries.getFirstBar() != lastBar
        typedTimeSeries.getLastBar() == lastBar
    }

    interface SuperBar extends Bar {
    }

    static class SuperBarImpl implements SuperBar {
        public ZonedDateTime endTime

        SuperBarImpl() {
        }

        SuperBarImpl(ZonedDateTime endTime) {
            this.endTime = endTime
        }

        @Override
        Num getOpenPrice() {
            return null
        }

        @Override
        Num getMinPrice() {
            return null
        }

        @Override
        Num getMaxPrice() {
            return null
        }

        @Override
        Num getClosePrice() {
            return null
        }

        @Override
        Num getVolume() {
            return null
        }

        @Override
        int getTrades() {
            return 0
        }

        @Override
        Num getAmount() {
            return null
        }

        @Override
        Duration getTimePeriod() {
            return null
        }

        @Override
        ZonedDateTime getBeginTime() {
            return null
        }

        @Override
        ZonedDateTime getEndTime() {
            return endTime
        }

        @Override
        void addTrade(Num tradeVolume, Num tradePrice) {

        }

        @Override
        void addPrice(Num price) {

        }
    }

    static class DifferentBarClass implements Bar {

        @Override
        Num getOpenPrice() {
            return null
        }

        @Override
        Num getMinPrice() {
            return null
        }

        @Override
        Num getMaxPrice() {
            return null
        }

        @Override
        Num getClosePrice() {
            return null
        }

        @Override
        Num getVolume() {
            return null
        }

        @Override
        int getTrades() {
            return 0
        }

        @Override
        Num getAmount() {
            return null
        }

        @Override
        Duration getTimePeriod() {
            return null
        }

        @Override
        ZonedDateTime getBeginTime() {
            return null
        }

        @Override
        ZonedDateTime getEndTime() {
            return null
        }

        @Override
        void addTrade(Num tradeVolume, Num tradePrice) {

        }

        @Override
        void addPrice(Num price) {

        }
    }
}