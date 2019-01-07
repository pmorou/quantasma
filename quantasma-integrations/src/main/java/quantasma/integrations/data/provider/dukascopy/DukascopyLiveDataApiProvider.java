package quantasma.integrations.data.provider.dukascopy;

import lombok.extern.slf4j.Slf4j;
import quantasma.core.TradeEngine;
import quantasma.integrations.data.provider.AbstractLiveDataProvider;
import quantasma.integrations.event.EventPublisher;

@Slf4j
public class DukascopyLiveDataApiProvider extends AbstractLiveDataProvider {

    private final DukascopyApiClient dukascopyClient;
    private final EventPublisher eventPublisher;

    private long strategyProcessId;

    public DukascopyLiveDataApiProvider(TradeEngine tradeEngine, DukascopyApiClient dukascopyClient, EventPublisher eventPublisher) {
        super(tradeEngine);
        this.dukascopyClient = dukascopyClient;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void run() {
        final TransferLiveDataStrategy liveDataStrategy = new TransferLiveDataStrategy(eventPublisher);
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
