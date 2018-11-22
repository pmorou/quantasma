package quantasma.core;

public class BaseDataService implements DataService {

    private final MarketData marketData;

    public BaseDataService(MarketData marketData) {
        this.marketData = marketData;
    }

    @Override
    public MarketData getMarketData() {
        return marketData;
    }
}
