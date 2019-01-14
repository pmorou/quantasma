package quantasma.core;

import quantasma.core.order.OpenMarketOrder;
import quantasma.core.order.CloseMarketOrder;

public class NullOrderService implements OrderService {
    @Override
    public void execute(OpenMarketOrder openMarketOrder) {
        // ignore
    }

    @Override
    public void execute(CloseMarketOrder closeMarkerOrder) {
        // ignore
    }
}
