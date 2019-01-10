package quantasma.app.config.service.integration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import quantasma.core.OrderService;
import quantasma.integrations.data.provider.LiveDataProvider;
import quantasma.integrations.data.provider.dukascopy.DukascopyApiClient;
import quantasma.integrations.data.provider.dukascopy.DukascopyLiveDataProvider;
import quantasma.integrations.data.provider.dukascopy.DukascopyOrderService;
import quantasma.integrations.event.EventPublisher;

@Configuration
@Profile("dukascopy")
public class DukascopyConfig {

    @Bean
    public DukascopyApiClient dukascopyClient(DukascopyLiveDataConfig liveDataConfig) {
        return new DukascopyApiClient(liveDataConfig.getUrl(),
                                      liveDataConfig.getUserName(),
                                      liveDataConfig.getPassword());
    }

    @Bean
    public LiveDataProvider dukascopyLiveDataProvider(DukascopyApiClient dukascopyClient, EventPublisher eventPublisher) {
        return new DukascopyLiveDataProvider(dukascopyClient, eventPublisher);
    }

    @Bean
    public OrderService dukascopyOrderService(DukascopyApiClient dukascopyApiClient) {
        return new DukascopyOrderService(dukascopyApiClient);
    }

    @Bean
    public DukascopyLiveDataConfig dukascopyLiveDataConfig(@Value("${live.data.provider.userName}") String userName,
                                                           @Value("${live.data.provider.password}") String password,
                                                           @Value("${live.data.provider.url}") String url) {
        return new DukascopyLiveDataConfig(userName, password, url);
    }

    @AllArgsConstructor
    @Getter
    public class DukascopyLiveDataConfig {
        private final String userName;
        private final String password;
        private final String url;
    }

}
