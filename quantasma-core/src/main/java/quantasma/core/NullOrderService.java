package quantasma.core;

import quantasma.core.order.OpenMarketOrder;
import quantasma.core.order.CloseMarkerOrder;

public class NullOrderService implements OrderService {
    @Override
    public void openPosition(OpenMarketOrder openMarketOrder) {
        // ignore
    }

    @Override
    public void closePosition(CloseMarkerOrder closeMarkerOrder) {
        // ignore
    }
}
