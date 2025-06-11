package quantasma.core.timeseries.bar;

import org.ta4j.core.Bar;
import org.ta4j.core.num.Num;
import quantasma.core.Quote;

public interface OneSidedBar extends Bar {

    default void updateBar(Quote quote) {
        addPrice(quote.getBid());
    }

    Num getOpenPrice();

    Num getLowPrice();

    Num getHighPrice();

    Num getClosePrice();
}
