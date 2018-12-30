package quantasma.core.timeseries.bar;

import org.ta4j.core.Bar;
import quantasma.core.timeseries.bar.wrapper.ForwardingBar;

public class BaseOneSidedBar extends ForwardingBar implements OneSidedBar {

    public BaseOneSidedBar(Bar bar) {
        super(bar);
    }

}
