package quantasma.app.feature.data.historical.provider.dukascopy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import quantasma.app.feature.data.historical.provider.HistoricalDataUpdater;
import quantasma.app.model.FeedBarsSettings;
import quantasma.app.service.HistoricalDataService;
import quantasma.integrations.data.provider.dukascopy.DukascopyApiClient;

@Component
@Profile("dukascopy")
@Slf4j
public class DukascopyHistoricalDataUpdater implements HistoricalDataUpdater {

    @Autowired
    private DukascopyApiClient dukascopyClient;

    @Autowired
    private HistoricalDataService historicalDataService;

    @Override
    public void update(FeedBarsSettings feedBarsSettings) {
        final long processId = dukascopyClient.runStrategy(new FetchHistoricalDataStrategy(historicalDataService, feedBarsSettings));
        dukascopyClient.stopStrategy(processId);
    }
}