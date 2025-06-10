package quantasma.app.config.mock;

import com.github.fakemongo.Fongo;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Mongo configuration for in-memory DB.
 *
 * Provides {@code MongoTemplate} for tests.
 */
@Configuration
@Profile("mock")
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class MockMongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        throw new UnsupportedOperationException("Not supported yet.");
//        return new MongoTemplate(new Fongo("mock").getMongo(), "mock");
    }

}
