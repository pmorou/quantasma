package quantasma.core;

import quantasma.core.order.CloseMarkerOrder;
import quantasma.core.order.OpenMarketOrder;

public class TestOrderService implements OrderService {
    private final OrderAmountRef orderAmountRef;

    public TestOrderService(OrderAmountRef orderAmountRef) {
        this.orderAmountRef = orderAmountRef;
    }

    @Override
    public void openPosition(OpenMarketOrder openMarketOrder) {

    }

    @Override
    public void closePosition(CloseMarkerOrder closeMarkerOrder) {

    }
}
