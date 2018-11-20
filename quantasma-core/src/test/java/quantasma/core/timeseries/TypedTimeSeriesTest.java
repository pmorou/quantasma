package quantasma.core.timeseries;

import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.num.Num;

import java.time.Duration;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TypedTimeSeriesTest {

    @Test(expected = ClassCastException.class)
    public void givenTimeSeriesTypedAsSuperBarWhenAddingSuperBarNonImplementingClassShouldThrowException() {
        // given
        final TypedTimeSeries<SuperBar> typedTimeSeries = TypedTimeSeries.create(SuperBar.class, new BaseTimeSeries());

        // when
        typedTimeSeries.addBar((SuperBar) new DifferentClass()); // forcing cast
    }

    @Test
    public void givenTimeSeriesTypedAsSuperBarWhenAddingSuperBarImplementingClassShouldReturn1AsBarCount() {
        // given
        final TypedTimeSeries<SuperBar> typedTimeSeries = TypedTimeSeries.create(SuperBar.class, new BaseTimeSeries());

        // when
        typedTimeSeries.addBar(new SuperBarImpl());

        // then
        assertThat(typedTimeSeries.getTimeSeries().getBarCount()).isEqualTo(1);
    }

    @Test
    public void givenTimeSeriesTypedAsSuperBarWhenAdding1SuperBarImplementingClassShouldReturnItAsFirstAndLastBar() {
        // given
        final TypedTimeSeries<SuperBar> typedTimeSeries = TypedTimeSeries.create(SuperBar.class, new BaseTimeSeries());
        final SuperBarImpl firstAndLastBar = new SuperBarImpl();

        // when
        typedTimeSeries.addBar(firstAndLastBar);

        // then
        assertThat(typedTimeSeries.getFirstBar() == firstAndLastBar).isTrue();
        assertThat(typedTimeSeries.getLastBar() == firstAndLastBar).isTrue();
    }


    @Test
    public void givenTimeSeriesTypedAsSuperBarWhenAdding2SuperBarImplementingClassesShouldReturnThemAsFirstAndLastBar() {
        // given
        final TypedTimeSeries<SuperBar> typedTimeSeries = TypedTimeSeries.create(SuperBar.class, new BaseTimeSeries());
        final SuperBarImpl firstBar = new SuperBarImpl(ZonedDateTime.now());
        final SuperBarImpl lastBar = new SuperBarImpl(ZonedDateTime.now().plusDays(2));

        // when
        typedTimeSeries.addBar(firstBar);
        typedTimeSeries.addBar(lastBar);

        // then
        assertThat(typedTimeSeries.getTimeSeries().getBarCount()).isEqualTo(2);
        assertThat(typedTimeSeries.getFirstBar() == firstBar).isTrue();
        assertThat(typedTimeSeries.getLastBar() != firstBar).isTrue();
        assertThat(typedTimeSeries.getFirstBar() != lastBar).isTrue();
        assertThat(typedTimeSeries.getLastBar() == lastBar).isTrue();
    }

    interface SuperBar extends Bar {
    }

    static class SuperBarImpl implements SuperBar {
        public ZonedDateTime endTime;

        public SuperBarImpl() {
        }

        public SuperBarImpl(ZonedDateTime endTime) {
            this.endTime = endTime;
        }

        @Override
        public Num getOpenPrice() {
            return null;
        }

        @Override
        public Num getMinPrice() {
            return null;
        }

        @Override
        public Num getMaxPrice() {
            return null;
        }

        @Override
        public Num getClosePrice() {
            return null;
        }

        @Override
        public Num getVolume() {
            return null;
        }

        @Override
        public int getTrades() {
            return 0;
        }

        @Override
        public Num getAmount() {
            return null;
        }

        @Override
        public Duration getTimePeriod() {
            return null;
        }

        @Override
        public ZonedDateTime getBeginTime() {
            return null;
        }

        @Override
        public ZonedDateTime getEndTime() {
            return endTime;
        }

        @Override
        public void addTrade(Num tradeVolume, Num tradePrice) {

        }

        @Override
        public void addPrice(Num price) {

        }
    }

    static class DifferentClass implements Bar {

        @Override
        public Num getOpenPrice() {
            return null;
        }

        @Override
        public Num getMinPrice() {
            return null;
        }

        @Override
        public Num getMaxPrice() {
            return null;
        }

        @Override
        public Num getClosePrice() {
            return null;
        }

        @Override
        public Num getVolume() {
            return null;
        }

        @Override
        public int getTrades() {
            return 0;
        }

        @Override
        public Num getAmount() {
            return null;
        }

        @Override
        public Duration getTimePeriod() {
            return null;
        }

        @Override
        public ZonedDateTime getBeginTime() {
            return null;
        }

        @Override
        public ZonedDateTime getEndTime() {
            return null;
        }

        @Override
        public void addTrade(Num tradeVolume, Num tradePrice) {

        }

        @Override
        public void addPrice(Num price) {

        }
    }
}