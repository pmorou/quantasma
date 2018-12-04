package quantasma.app.config.service.historical;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class HistoricalDataServiceProperties {

    private final String prefix;

    public HistoricalDataServiceProperties(@Value("service.historical-data") String prefix) {
        this.prefix = prefix;
    }

    public String collectionName() {
        return prefix + "_OHLCV";
    }
}
