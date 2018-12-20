package quantasma.integrations.data.provider.dukascopy;

import lombok.extern.slf4j.Slf4j;
import quantasma.integrations.event.EventSink;
import quantasma.integrations.data.provider.AbstractLiveDataProvider;
import quantasma.core.TradeEngine;

@Slf4j
public class DukascopyLiveDataApiProvider extends AbstractLiveDataProvider {

    private final DukascopyApiClient dukascopyClient;
    private final EventSink eventSink;

    private long strategyProcessId;

    public DukascopyLiveDataApiProvider(TradeEngine tradeEngine, DukascopyApiClient dukascopyClient, EventSink eventSink) {
        super(tradeEngine);
        this.dukascopyClient = dukascopyClient;
        this.eventSink = eventSink;
    }

    @Override
    public void run() {
        final TransferLiveDataStrategy liveDataStrategy = new TransferLiveDataStrategy(eventSink);
        strategyProcessId = dukascopyClient.runStrategy(liveDataStrategy);
    }

    @Override
    public void stop() {
        dukascopyClient.stopStrategy(strategyProcessId);
    }

    @Override
    public boolean isRunning() {
        return dukascopyClient.getClient().getStartedStrategies().containsKey(strategyProcessId);
    }
}
