package quantasma.integrations.data.provider.dukascopy;

import com.dukascopy.api.IAccount;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IMessage;
import com.dukascopy.api.IStrategy;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.Period;
import lombok.extern.slf4j.Slf4j;
import quantasma.core.TradeEngine;

import java.time.Instant;
import java.time.ZoneOffset;

@Slf4j
public class TransferLiveDataStrategy implements IStrategy {

    private final TradeEngine tradeEngine;

    public TransferLiveDataStrategy(TradeEngine strategyRegister) {
        this.tradeEngine = strategyRegister;
    }

    public void onStart(IContext context) throws JFException {
        log.info("Starting live data strategy");
    }

    public void onAccount(IAccount account) throws JFException {
    }

    public void onMessage(IMessage message) throws JFException {
    }

    public void onStop() throws JFException {
    }

    public void onTick(Instrument instrument, ITick tick) throws JFException {
        tradeEngine.process("EURUSD",
                            Instant.ofEpochMilli(tick.getTime()).atZone(ZoneOffset.UTC),
                            tick.getBid(),
                            tick.getAsk());
    }

    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
    }

}