package quantasma.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import quantasma.app.config.service.integration.DukascopyLiveDataConfig;
import quantasma.core.Context;
import quantasma.core.StrategyControl;
import quantasma.core.TradeEngine;
import quantasma.core.TradeStrategy;
import quantasma.core.analysis.parametrize.Values;
import quantasma.examples.RSIStrategy;
import quantasma.integrations.data.provider.LiveDataProvider;
import quantasma.integrations.data.provider.dukascopy.DukascopyApiClient;
import quantasma.integrations.data.provider.dukascopy.DukascopyLiveDataApiProvider;
import quantasma.integrations.event.EventPublisher;

@Configuration
@Slf4j
public class IntegrationServiceConfig {

    @Bean
    public TradeStrategy rsiStrategy(StrategyControl strategyControl, Context context) {
        final Values<RSIStrategy.Parameter> parameterValues =
                Values.of(RSIStrategy.Parameter.class)
                      .set(RSIStrategy.Parameter.TRADE_SYMBOL, "EURUSD")
                      .set(RSIStrategy.Parameter.RSI_PERIOD, 14)
                      .set(RSIStrategy.Parameter.RSI_LOWER_BOUND, 30)
                      .set(RSIStrategy.Parameter.RSI_UPPER_BOUND, 70);
        final TradeStrategy strategy = RSIStrategy.buildBullish(context, parameterValues);
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
    public EventPublisher eventPublisher() {
        return EventPublisher.instance();
    }

    @Bean
    @Profile("dukascopy")
    public LiveDataProvider dukascopyLiveDataProvider(DukascopyApiClient dukascopyClient, TradeEngine tradeEngine, EventPublisher eventPublisher) {
        return new DukascopyLiveDataApiProvider(tradeEngine, dukascopyClient, eventPublisher);
    }

    @Autowired
    public void runLiveDataProvider(@Value("${service.live-data.enabled}") boolean isLiveDataEnabled, LiveDataProvider liveDataProvider) {
        log.info("Is live data enabled: {}", isLiveDataEnabled);
        if (isLiveDataEnabled) {
            liveDataProvider.run();
        }
    }
}
