package quantasma.app.config.service.historical;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoProperties {

    private final String prefix;

    public MongoProperties(@Value("${service.historical-data.prefix}") String prefix) {
        this.prefix = prefix;
    }

    public String collectionName() {
        return prefix + "_OHLCV";
    }
}
