package quantasma.core.timeseries.bar;

import org.ta4j.core.num.Num;
import quantasma.core.Quote;

public interface BidAskBar extends OneSidedBar {

    @Override
    default void updateBar(Quote quote) {
        addPrice(quote.getBid(), quote.getAsk());
    }

    default Num getBidOpenPrice() {
        return getOpenPrice();
    }

    default Num getBidMinPrice() {
        return getLowPrice();
    }

    default Num getBidMaxPrice() {
        return getHighPrice();
    }

    default Num getBidClosePrice() {
        return getClosePrice();
    }

    Num getAskOpenPrice();

    Num getAskMinPrice();

    Num getAskMaxPrice();

    Num getAskClosePrice();

    void addPrice(Num bid, Num ask);
}
