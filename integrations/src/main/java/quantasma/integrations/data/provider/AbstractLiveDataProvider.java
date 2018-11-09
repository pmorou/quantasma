package quantasma.integrations.data.provider;

import quantasma.trade.engine.TradeEngine;

public abstract class AbstractLiveDataProvider implements LiveDataProvider {

    protected final TradeEngine tradeEngine;

    public AbstractLiveDataProvider(TradeEngine tradeEngine) {
        this.tradeEngine = tradeEngine;
    }

}
