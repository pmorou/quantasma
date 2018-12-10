package quantasma.integrations.data.provider.dukascopy;

import com.dukascopy.api.IAccount;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IEngine;
import com.dukascopy.api.IHistory;
import com.dukascopy.api.IMessage;
import com.dukascopy.api.IOrder;
import com.dukascopy.api.IStrategy;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.Period;
import lombok.extern.slf4j.Slf4j;
import quantasma.core.Quote;
import quantasma.integrations.event.AccountState;
import quantasma.integrations.event.Direction;
import quantasma.integrations.event.Event;
import quantasma.integrations.event.EventSink;
import quantasma.integrations.event.OpenedPosition;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class TransferLiveDataStrategy implements IStrategy {

    private final EventSink eventSink;

    private IEngine engine;
    private IHistory history;
    private IAccount account;

    public TransferLiveDataStrategy(EventSink eventSink) {
        this.eventSink = eventSink;
    }

    public void onStart(IContext context) throws JFException {
        log.info("Starting live data strategy");
        engine = context.getEngine();
        history = context.getHistory();
        account = context.getAccount();
    }

    public void onAccount(IAccount account) throws JFException {
        double profitLoss = 0;
        double totalAmount = 0;

        final List<OpenedPosition> openedPositions = new LinkedList<>();
        for (IOrder order : engine.getOrders()) {
            if (order.getState() == IOrder.State.FILLED) {
                profitLoss += order.getProfitLossInAccountCurrency();
                totalAmount += order.getAmount();

                openedPositions.add(new OpenedPosition(
                        order.getInstrument().getName(),
                        order.getOrderCommand().isLong() ? Direction.LONG : Direction.SHORT,
                        order.getAmount(),
                        order.getOpenPrice(),
                        order.getStopLossPrice(),
                        order.getTakeProfitPrice(),
                        order.getProfitLossInPips(),
                        order.getProfitLossInAccountCurrency()));
            }
        }

        eventSink.flush(Event.openedPositions(openedPositions));
        eventSink.flush(Event.accountState(
                new AccountState(history.getEquity(),
                                 account.getBalance(),
                                 profitLoss,
                                 totalAmount,
                                 account.getUsedMargin(),
                                 account.getAccountCurrency().getSymbol(),
                                 account.getLeverage())));
    }

    public void onMessage(IMessage message) throws JFException {
    }

    public void onStop() throws JFException {
    }

    public void onTick(Instrument instrument, ITick tick) throws JFException {
        eventSink.flush(Event.quote(
                new Quote(instrument.getPrimaryJFCurrency().getCurrencyCode() + instrument.getSecondaryJFCurrency().getCurrencyCode(),
                          Instant.ofEpochMilli(tick.getTime()).atZone(ZoneOffset.UTC),
                          tick.getBid(),
                          tick.getAsk())));
        onAccount(account);
    }

    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
    }

}