package quantasma.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quantasma.integrations.data.provider.LiveDataProvider;
import quantasma.integrations.event.EventPublisher;

@Configuration
@Slf4j
public class IntegrationServiceConfig {

    @Bean
    public EventPublisher eventPublisher() {
        return EventPublisher.instance();
    }

    @Autowired
    public void runLiveDataProvider(@Value("${service.live-data.enabled}") boolean isLiveDataEnabled, LiveDataProvider liveDataProvider) {
        log.info("Is live data enabled: {}", isLiveDataEnabled);
        if (isLiveDataEnabled) {
            liveDataProvider.run();
        }
    }

}
