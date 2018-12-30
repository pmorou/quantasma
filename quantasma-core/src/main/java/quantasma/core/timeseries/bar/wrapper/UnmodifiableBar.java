package quantasma.core.timeseries.bar.wrapper;

import org.ta4j.core.Bar;
import org.ta4j.core.num.Num;

public final class UnmodifiableBar extends ForwardingBar {

    public UnmodifiableBar(Bar bar) {
        super(bar);
    }

    @Override
    public void addTrade(Num tradeVolume, Num tradePrice) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPrice(Num price) {
        throw new UnsupportedOperationException();
    }

}
