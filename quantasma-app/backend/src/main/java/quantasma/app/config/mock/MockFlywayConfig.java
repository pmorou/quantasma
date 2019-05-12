package quantasma.app.config.mock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Disables Flyway auto configuration.
 *
 * Reason:
 * Flyway migration has been running everytime when an integration test was executed resulting in Travis CI build fail.
 */
@Configuration
@Profile("mock")
@EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@Slf4j
public class MockFlywayConfig implements CommandLineRunner {
    @Override
    public void run(String... args) {
        log.info("Disabling Flyway auto configuration");
    }
}
