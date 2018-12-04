package quantasma.app.feature.data.historical.provider.dukascopy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import quantasma.app.service.OhlcvTickService;
import quantasma.integrations.data.provider.dukascopy.DukascopyApiClient;

@Component
@Profile("dukascopy")
@Slf4j
public class HistoricalDataUpdater implements CommandLineRunner {

    @Autowired
    private DukascopyApiClient dukascopyClient;

    @Autowired
    private OhlcvTickService ohlcvTickService;

    @Value("${service.historical-data.enabled}")
    private boolean isHistoricServiceEnabled;

    @Override
    public void run(String... args) {
        log.info("Is history service enabled: {}", isHistoricServiceEnabled);
        if (!isHistoricServiceEnabled) {
            return;
        }

        final long processId = dukascopyClient.runStrategy(new FetchHistoricalDataStrategy(ohlcvTickService));
        dukascopyClient.stopStrategy(processId);
    }
}