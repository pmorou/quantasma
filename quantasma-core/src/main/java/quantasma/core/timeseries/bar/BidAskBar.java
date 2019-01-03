package quantasma.core.timeseries.bar;

import org.ta4j.core.num.Num;
import quantasma.core.Quote;

import java.util.function.Function;

public interface BidAskBar extends OneSidedBar {

    @Override
    default void updateBar(Quote quote, Function<Number, Num> numFunction) {
        addPrice(numFunction.apply(quote.getBid()), numFunction.apply(quote.getAsk()));
    }

    default Num getBidOpenPrice() {
        return getOpenPrice();
    }

    default Num getBidMinPrice() {
        return getMinPrice();
    }

    default Num getBidMaxPrice() {
        return getMaxPrice();
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
