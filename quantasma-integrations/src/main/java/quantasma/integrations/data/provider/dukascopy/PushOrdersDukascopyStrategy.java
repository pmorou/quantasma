package quantasma.integrations.data.provider.dukascopy;

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
import quantasma.core.order.CloseMarketOrder;
import quantasma.core.order.OpenMarketOrder;

@Slf4j
public class PushOrdersDukascopyStrategy implements IStrategy {

    private IEngine engine;

    @Override
    public void onStart(IContext context) {
        log.info("Starting push orders dukascopy strategy");
        engine = context.getEngine();
    }

    public void submit(OpenMarketOrder openMarketOrder) throws JFException {
        engine.submitOrder(openMarketOrder.getLabel(),
                           Instrument.valueOf(openMarketOrder.getSymbol()),
                           IEngine.OrderCommand.BUY,
                           openMarketOrder.getVolume());
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