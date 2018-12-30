package quantasma.core.timeseries.bar;

import org.ta4j.core.Bar;

public class BaseOneSideBar extends ForwardingBar implements OneSideBar {

    public BaseOneSideBar(Bar bar) {
        super(bar);
    }

}
