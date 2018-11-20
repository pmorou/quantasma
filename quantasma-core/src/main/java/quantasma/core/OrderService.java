package quantasma.core;

import quantasma.core.order.CloseMarkerOrder;
import quantasma.core.order.OpenMarketOrder;

public interface OrderService {
    void openPosition(OpenMarketOrder openMarketOrder);

    void closePosition(CloseMarkerOrder closeMarkerOrder);
}
