package quantasma.integrations.data.provider.dukascopy;

import lombok.extern.slf4j.Slf4j;
import quantasma.integrations.data.provider.LiveDataProvider;
import quantasma.integrations.event.EventPublisher;

@Slf4j
public class DukascopyLiveDataProvider implements LiveDataProvider {

    private final DukascopyApiClient dukascopyClient;
    private final EventPublisher eventPublisher;

    private long strategyProcessId;

    public DukascopyLiveDataProvider(DukascopyApiClient dukascopyClient, EventPublisher eventPublisher) {
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
