package quantasma.core.timeseries.bar;

import org.ta4j.core.Bar;

public class BaseOneSidedBar extends ForwardingBar implements OneSidedBar {

    public BaseOneSidedBar(Bar bar) {
        super(bar);
    }

}
