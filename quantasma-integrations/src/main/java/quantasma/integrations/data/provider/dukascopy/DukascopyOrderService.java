package quantasma.integrations.data.provider.dukascopy;

import com.dukascopy.api.JFException;
import lombok.extern.slf4j.Slf4j;
import quantasma.core.OrderService;
import quantasma.core.order.CloseMarketOrder;
import quantasma.core.order.MarketOrder;
import quantasma.core.order.OpenMarketOrder;

@Slf4j
public class DukascopyOrderService implements OrderService {
    private final PushOrdersDukascopyStrategy pushOrdersStrategy;

    public DukascopyOrderService(DukascopyApiClient dukascopyApiClient) {
        this.pushOrdersStrategy = new PushOrdersDukascopyStrategy();
        dukascopyApiClient.runStrategy(pushOrdersStrategy);
    }

    @Override
    public void execute(OpenMarketOrder openMarketOrder) {
        try {
            pushOrdersStrategy.submit(openMarketOrder);
        } catch (JFException e) {
            logSubmitError(openMarketOrder, e);
        }
    }

    @Override
    public void execute(CloseMarketOrder closeMarkerOrder) {
        try {
            pushOrdersStrategy.submit(closeMarkerOrder);
        } catch (JFException e) {
            logSubmitError(closeMarkerOrder, e);
        }
    }

    private void logSubmitError(MarketOrder marketOrder, JFException e) {
        log.error("Couldn't submit [" + marketOrder + "]", e);
    }
}
