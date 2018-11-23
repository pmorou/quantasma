package quantasma.core;

import lombok.Getter;

public class BaseDataService implements DataService {

    @Getter
    private final MarketData marketData;

    public BaseDataService(MarketData marketData) {
        this.marketData = marketData;
    }

}
