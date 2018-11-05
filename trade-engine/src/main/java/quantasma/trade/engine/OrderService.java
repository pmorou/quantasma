package quantasma.trade.engine;

import quantasma.trade.engine.order.CloseMarkerOrder;
import quantasma.trade.engine.order.OpenMarketOrder;

public interface OrderService {
    void openPosition(OpenMarketOrder openMarketOrder);

    void closePosition(CloseMarkerOrder closeMarkerOrder);
}
