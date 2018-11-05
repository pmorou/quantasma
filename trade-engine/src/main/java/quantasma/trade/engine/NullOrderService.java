package quantasma.trade.engine;

import quantasma.trade.engine.order.CloseMarkerOrder;
import quantasma.trade.engine.order.OpenMarketOrder;

public class NullOrderService implements OrderService {
    @Override
    public void openPosition(OpenMarketOrder openMarketOrder) {
    }

    @Override
    public void closePosition(CloseMarkerOrder closeMarkerOrder) {
    }
}
