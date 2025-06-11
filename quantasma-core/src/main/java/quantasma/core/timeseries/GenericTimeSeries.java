package quantasma.core.timeseries;

import org.ta4j.core.BarSeries;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;
import quantasma.core.BarPeriod;
import quantasma.core.timeseries.bar.BarFactory;
import quantasma.core.timeseries.bar.OneSidedBar;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface GenericTimeSeries<B extends OneSidedBar> {

    /**
     * Returns unmodifiable view of plain {@link org.ta4j.core.BarSeries} based on {@link org.ta4j.core.Bar}.
     *
     * @return TimeSeries
     * @implSpec Any mutation or replace action on inner objects (eg. {@code Bar}) will result in {@code UnsupportedOperationException}
     */
    BarSeries plainTimeSeries();

    BarPeriod getBarPeriod();

    String getSymbol();

    BarFactory<B> getBarFactory();

    B getBar(int i);

    default B getFirstBar() {
        return getBar(getBeginIndex());
    }

    default B getLastBar() {
        return getBar(getEndIndex());
    }

    default void addBar() {
        addBar(getBarFactory().create(getBarPeriod(), numFactory()));
    }

    default void addBar(B bar) {
        addBar(bar, false);
    }

    void addBar(B bar, boolean replace);

    default void addTrade(Number tradeVolume, Number tradePrice) {
        addTrade(numFactory().numOf(tradeVolume), numFactory().numOf(tradePrice));
    }

    default void addTrade(String tradeVolume, String tradePrice) {
        addTrade(numFactory().numOf(new BigDecimal(tradeVolume)), numFactory().numOf(new BigDecimal(tradePrice)));
    }

    void addTrade(Num tradeVolume, Num tradePrice);

    void addPrice(Num price);

    default void addPrice(String price) {
        addPrice(new BigDecimal(price));
    }

    default void addPrice(Number price) {
        addPrice(numFactory().numOf(price));
    }

    String getName();

    int getBarCount();

    default boolean isEmpty() {
        return getBarCount() == 0;
    }

    int getBeginIndex();

    int getEndIndex();

    default String getSeriesPeriodDescription() {
        StringBuilder sb = new StringBuilder();
        if (!isEmpty()) {
            OneSidedBar firstBar = getFirstBar();
            OneSidedBar lastBar = getLastBar();
            sb.append(firstBar.getEndTime().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE_TIME))
                .append(" - ")
                .append(lastBar.getEndTime().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE_TIME));
        }
        return sb.toString();
    }

    void setMaximumBarCount(int maximumBarCount);

    int getMaximumBarCount();

    int getRemovedBarsCount();

    NumFactory numFactory();
}
