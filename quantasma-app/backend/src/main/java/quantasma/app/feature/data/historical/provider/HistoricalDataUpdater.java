package quantasma.app.feature.data.historical.provider;

import quantasma.app.model.FeedBarsSettings;

public interface HistoricalDataUpdater {
    void update(FeedBarsSettings feedBarsSettings);
}
