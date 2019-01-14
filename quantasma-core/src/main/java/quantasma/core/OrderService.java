package quantasma.core;

import quantasma.core.order.CloseMarketOrder;
import quantasma.core.order.OpenMarketOrder;

public interface OrderService {
    void execute(OpenMarketOrder openMarketOrder);

    void execute(CloseMarketOrder closeMarkerOrder);
}
