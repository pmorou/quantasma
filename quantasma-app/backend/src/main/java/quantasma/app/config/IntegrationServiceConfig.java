package quantasma.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quantasma.core.TradeEngine;
import quantasma.integrations.data.provider.LiveDataProvider;
import quantasma.integrations.event.EventPublisher;
import quantasma.integrations.event.QuoteEventSubscriber;

@Configuration
@Slf4j
public class IntegrationServiceConfig {

    @Bean
    public EventPublisher eventPublisher(TradeEngine tradeEngine) {
        final EventPublisher eventPublisher = EventPublisher.instance();
        log.info("Subscribing trade engine to quotes");
        eventPublisher.subscribe(new QuoteEventSubscriber(tradeEngine));
        return eventPublisher;
    }

    @Autowired
    public void runLiveDataProvider(@Value("${service.live-data.enabled}") boolean isLiveDataEnabled, LiveDataProvider liveDataProvider) {
        log.info("Is live data enabled: {}", isLiveDataEnabled);
        if (isLiveDataEnabled) {
            liveDataProvider.run();
        }
    }

}
