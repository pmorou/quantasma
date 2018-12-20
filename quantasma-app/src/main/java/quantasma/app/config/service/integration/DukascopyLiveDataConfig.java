package quantasma.app.config.service.integration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dukascopy")
@Component
@Getter
public class DukascopyLiveDataConfig {

    private final String userName;
    private final String password;
    private final String url;

    public DukascopyLiveDataConfig(@Value("${live.data.provider.userName}") String userName,
                                   @Value("${live.data.provider.password}") String password,
                                   @Value("${live.data.provider.url}") String url) {
        this.userName = userName;
        this.password = password;
        this.url = url;
    }
}
