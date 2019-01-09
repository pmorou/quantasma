package quantasma.core;

import quantasma.core.order.CloseMarkerOrder;
import quantasma.core.order.OpenMarketOrder;

public interface OrderService {
    void execute(OpenMarketOrder openMarketOrder);

    void execute(CloseMarkerOrder closeMarkerOrder);
}
