package quantasma.app.config.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import quantasma.app.feature.data.historical.provider.HistoricalDataUpdater;
import quantasma.app.model.FeedBarsSettings;

@Component
@Profile("mock")
public class MockHistoricalDataUpdater implements HistoricalDataUpdater {
    @Override
    public void update(FeedBarsSettings feedBarsSettings) {
        // mock
    }
}
