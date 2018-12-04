package quantasma.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import quantasma.app.config.service.integration.DukascopyLiveDataConfig;
import quantasma.core.BarPeriod;
import quantasma.core.Context;
import quantasma.core.StrategyControl;
import quantasma.core.TradeEngine;
import quantasma.core.TradeStrategy;
import quantasma.examples.RSIStrategy;
import quantasma.integrations.data.provider.LiveDataProvider;
import quantasma.integrations.data.provider.dukascopy.DukascopyApiClient;
import quantasma.integrations.data.provider.dukascopy.DukascopyLiveDataApiProvider;

@Configuration
@Slf4j
public class IntegrationServiceConfig {

    @Bean
    public TradeStrategy rsiStrategy(StrategyControl strategyControl, Context context) {
        final TradeStrategy strategy = RSIStrategy.buildBullish(context, "EURUSD", BarPeriod.M1);
        strategyControl.register(strategy);
        return strategy;
    }

    @Bean
    @Profile("dukascopy")
    public DukascopyApiClient dukascopyClient(DukascopyLiveDataConfig liveDataConfig) {
        return new DukascopyApiClient(liveDataConfig.getUrl(),
                                      liveDataConfig.getUserName(),
                                      liveDataConfig.getPassword());
    }

    @Bean
    @Profile("dukascopy")
    public LiveDataProvider dukascopyLiveDataProvider(DukascopyApiClient dukascopyClient, TradeEngine tradeEngine) {
        return new DukascopyLiveDataApiProvider(tradeEngine, dukascopyClient);
    }

    @Autowired
    public void runLiveDataProvider(@Value("${service.live-data.enabled}") boolean isLiveDataEnabled, LiveDataProvider liveDataProvider) {
        log.info("Is live data enabled: {}", isLiveDataEnabled);
        if (isLiveDataEnabled) {
            liveDataProvider.run();
        }
    }
}
