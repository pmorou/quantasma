package quantasma.integrations.data.provider.dukascopy;

import com.dukascopy.api.JFException;
import lombok.extern.slf4j.Slf4j;
import quantasma.core.OrderService;
import quantasma.core.order.CloseMarketOrder;
import quantasma.core.order.MarketOrder;
import quantasma.core.order.OpenMarketOrder;
import quantasma.integrations.data.provider.dukascopy.strategy.OrderPublisher;

@Slf4j
public class DukascopyOrderService implements OrderService {
    private final OrderPublisher orderPublisher;

    public DukascopyOrderService(DukascopyApiClient dukascopyApiClient) {
        this.orderPublisher = new OrderPublisher();
        dukascopyApiClient.runStrategy(orderPublisher);
    }

    @Override
    public void execute(OpenMarketOrder openMarketOrder) {
        logExecuting(openMarketOrder);
        try {
            orderPublisher.submit(openMarketOrder);
        } catch (JFException e) {
            logFailedExecution(openMarketOrder, e);
        }
    }

    @Override
    public void execute(CloseMarketOrder closeMarkerOrder) {
        logExecuting(closeMarkerOrder);
        try {
            orderPublisher.submit(closeMarkerOrder);
        } catch (JFException e) {
            logFailedExecution(closeMarkerOrder, e);
        }
    }

    private void logExecuting(MarketOrder openMarketOrder) {
        log.info("Executing [{}]", openMarketOrder);
    }

    private void logFailedExecution(MarketOrder marketOrder, JFException e) {
        log.error("Failed execution of [" + marketOrder + "]", e);
    }
}
