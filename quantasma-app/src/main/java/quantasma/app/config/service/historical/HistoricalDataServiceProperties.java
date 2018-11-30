package quantasma.app.config.service.historical;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("service.historical-data")
public class HistoricalDataServiceProperties {

    private final String prefix;

    public String collectionName() {
        return prefix + "_OHLCV";
    }
}
