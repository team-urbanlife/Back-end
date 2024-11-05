package com.wegotoo.config.embedded;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
@RequiredArgsConstructor
@DependsOn("portInspector")
@Profile("local")
public class EmbeddedMongoConfig {

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.database}")
    private String database;

    private final PortInspector portInspector;

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoServer mongoServer) {
        String connection = mongoServer.getConnectionString();
        return new SimpleMongoClientDatabaseFactory(appendDatabaseName(connection));
    }

    @Bean(destroyMethod = "shutdown")
    public MongoServer mongoServer() throws IOException {
        MongoServer mongoServer = new MongoServer(new MemoryBackend());
        int availablePort = portInspector.isPortRunning(port) ? portInspector.findAvailablePort() : port;
        mongoServer.bind(host, availablePort);
        return mongoServer;
    }

    private String appendDatabaseName(String baseConnection) {
        return String.format("%s/%s", baseConnection, database);
    }

}
