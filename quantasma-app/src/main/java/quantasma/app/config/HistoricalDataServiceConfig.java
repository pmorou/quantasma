package quantasma.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import quantasma.app.config.service.historical.MongoConfig;

@Configuration
@Import(MongoConfig.class)
public class HistoricalDataServiceConfig {

}
