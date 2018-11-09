package quantasma.integrations.data.provider.dukascopy;

import lombok.extern.slf4j.Slf4j;
import quantasma.integrations.data.provider.AbstractLiveDataProvider;
import quantasma.trade.engine.TradeEngine;

@Slf4j
public class DukascopyLiveDataApiProvider extends AbstractLiveDataProvider {

    private final DukascopyApiClient dukascopyClient;

    private long strategyProcessId;

    public DukascopyLiveDataApiProvider(TradeEngine tradeEngine, DukascopyApiClient dukascopyClient) {
        super(tradeEngine);
        this.dukascopyClient = dukascopyClient;
    }

    @Override
    public void run() {
        final TransferLiveDataStrategy liveDataStrategy = new TransferLiveDataStrategy(tradeEngine);
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
