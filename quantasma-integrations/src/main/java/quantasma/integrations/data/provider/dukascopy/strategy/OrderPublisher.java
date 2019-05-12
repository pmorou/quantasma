package quantasma.integrations.data.provider.dukascopy.strategy;

import com.dukascopy.api.IAccount;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IEngine;
import com.dukascopy.api.IMessage;
import com.dukascopy.api.IOrder;
import com.dukascopy.api.IStrategy;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.Period;
import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.Order;
import quantasma.core.order.CloseMarketOrder;
import quantasma.core.order.OpenMarketOrder;

@Slf4j
public class OrderPublisher implements IStrategy {

    private IEngine engine;

    @Override
    public void onStart(IContext context) {
        log.info("Starting {} dukascopy strategy", getClass().getSimpleName());
        engine = context.getEngine();
    }

    public void submit(OpenMarketOrder openMarketOrder) throws JFException {
        final IOrder executedOrder = engine.submitOrder(openMarketOrder.getLabel(),
            Instrument.valueOf(openMarketOrder.getSymbol()),
            resolveOrderCommand(openMarketOrder.getOrderType()),
            openMarketOrder.getVolume());
        if (executedOrder == null) {
            throw new IllegalStateException(String.format("Order [%s] not executed", openMarketOrder));
        }
    }

    private static IEngine.OrderCommand resolveOrderCommand(Order.OrderType orderType) {
        switch (orderType) {
            case BUY:
                return IEngine.OrderCommand.BUY;
            case SELL:
                return IEngine.OrderCommand.SELL;
        }
        throw new IllegalArgumentException(String.format("Unknown order type [%s]", orderType));
    }

    public void submit(CloseMarketOrder closeMarkerOrder) throws JFException {
        final IOrder order = engine.getOrder(closeMarkerOrder.getLabel());
        if (order == null) {
            throw new IllegalArgumentException(String.format("No order found with label [%s]", closeMarkerOrder.getLabel()));
        }
        engine.closeOrders(order);
    }

    @Override
    public void onAccount(IAccount account) {
        // ignore
    }

    @Override
    public void onMessage(IMessage message) {
        // ignore
    }

    @Override
    public void onStop() {
        // ignore
    }

    @Override
    public void onTick(Instrument instrument, ITick tick) {
        // ignore
    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) {
        // ignore
    }

}