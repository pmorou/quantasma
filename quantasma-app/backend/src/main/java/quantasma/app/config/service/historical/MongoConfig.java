package quantasma.app.config.service.historical;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.index.IndexDefinition;

@Slf4j
@Configuration
@Profile("!mock")
public class MongoConfig {

    private final MongoProperties mongoProperties;

    @Autowired
    public MongoConfig(MongoProperties mongoProperties) {
        this.mongoProperties = mongoProperties;
    }

    @Autowired
    public void ensureIndexes(MongoOperations mongoOperations) {
        final Document doc = new Document()
            .append("date", 1)
            .append("symbol", 1)
            .append("period", 1);
        final IndexDefinition compoundIndexDefinition = new CompoundIndexDefinition(doc).unique();
        log.info("Ensuring [{}] index on the [{}] collection.", compoundIndexDefinition, mongoProperties.collectionName());
        mongoOperations.indexOps(mongoProperties.collectionName()).ensureIndex(compoundIndexDefinition);
    }

}
