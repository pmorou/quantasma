package quantasma.core;

import quantasma.core.order.OpenMarketOrder;
import quantasma.core.order.CloseMarketOrder;

/**
 * No-op implementation
 */
public class NoOpOrderService implements OrderService {
    @Override
    public void execute(OpenMarketOrder ignored) {
        // ignore
    }

    @Override
    public void execute(CloseMarketOrder ignored) {
        // ignore
    }
}
