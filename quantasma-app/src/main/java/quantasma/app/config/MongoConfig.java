package quantasma.app.config;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.index.IndexDefinition;

@Configuration
@Slf4j
public class MongoConfig {

    @Autowired
    public void ensureIndexes(MongoOperations mongoOperations, @Value("${service.historical-data.collection-prefix}") String collectionPrefix) {
        final Document doc = new Document()
                .append("date", 1)
                .append("symbol", 1)
                .append("period", 1);
        final IndexDefinition compoundIndexDefinition = new CompoundIndexDefinition(doc).unique();
        final String collectionName = collectionName(collectionPrefix);
        log.info("Ensuring [{}] index on the [{}] collection.", compoundIndexDefinition, collectionName);
        mongoOperations.indexOps(collectionName).ensureIndex(compoundIndexDefinition);
    }

    private static String collectionName(String collectionPrefix) {
        return collectionPrefix + "_OHLCV";
    }
}
