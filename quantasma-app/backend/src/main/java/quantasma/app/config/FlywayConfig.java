package quantasma.app.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    public FlywayConfig(@Value("${spring.datasource.url}") String url,
                        @Value("${spring.datasource.username}") String username,
                        @Value("${spring.datasource.password}") String password) {
        Flyway flyway = Flyway.configure().dataSource(url, username, password).load();
        flyway.migrate();
    }

}
