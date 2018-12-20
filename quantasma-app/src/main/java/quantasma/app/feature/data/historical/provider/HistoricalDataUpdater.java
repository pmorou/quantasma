package quantasma.app.feature.data.historical.provider;

import quantasma.app.model.PushTicksSettings;

public interface HistoricalDataUpdater {
    void update(PushTicksSettings pushTicksSettings);
}
