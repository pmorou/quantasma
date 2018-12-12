package quantasma.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import quantasma.app.config.service.integration.DukascopyLiveDataConfig;
import quantasma.app.service.EventsService;
import quantasma.core.Context;
import quantasma.core.StrategyControl;
import quantasma.core.TradeEngine;
import quantasma.core.TradeStrategy;
import quantasma.core.analysis.parametrize.Parameters;
import quantasma.examples.RSIStrategy;
import quantasma.integrations.data.provider.LiveDataProvider;
import quantasma.integrations.data.provider.dukascopy.DukascopyApiClient;
import quantasma.integrations.data.provider.dukascopy.DukascopyLiveDataApiProvider;
import quantasma.integrations.event.AccountStateEvent;
import quantasma.integrations.event.EventSink;
import quantasma.integrations.event.OpenedPositionsEvent;
import quantasma.integrations.event.QuoteEvent;

@Configuration
@Slf4j
public class IntegrationServiceConfig {

    @Bean
    public TradeStrategy rsiStrategy(StrategyControl strategyControl, Context context) {
        final Parameters parameters = Parameters.from(RSIStrategy.ParameterList.class)
                .add(RSIStrategy.ParameterList.TRADE_SYMBOL, "EURUSD")
                .add(RSIStrategy.ParameterList.RSI_PERIOD, 14)
                .add(RSIStrategy.ParameterList.RSI_LOWER_BOUND, 30)
                .add(RSIStrategy.ParameterList.RSI_UPPER_BOUND, 70);
        final TradeStrategy strategy = RSIStrategy.buildBullish(context, parameters);
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
    public EventSink eventSink(EventsService eventsService) {
        return EventSink.instance()
                        .pipe(QuoteEvent.class, eventsService::publish)
                        .pipe(AccountStateEvent.class, eventsService::publish)
                        .pipe(OpenedPositionsEvent.class, eventsService::publish);
    }

    @Bean
    @Profile("dukascopy")
    public LiveDataProvider dukascopyLiveDataProvider(DukascopyApiClient dukascopyClient, TradeEngine tradeEngine, EventSink eventSink) {
        return new DukascopyLiveDataApiProvider(tradeEngine, dukascopyClient, eventSink);
    }

    @Autowired
    public void runLiveDataProvider(@Value("${service.live-data.enabled}") boolean isLiveDataEnabled, LiveDataProvider liveDataProvider) {
        log.info("Is live data enabled: {}", isLiveDataEnabled);
        if (isLiveDataEnabled) {
            liveDataProvider.run();
        }
    }
}
