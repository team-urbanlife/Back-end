package com.wegotoo.config;

import static io.micrometer.common.util.StringUtils.isEmpty;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
@Profile("local")
public class EmbeddedMongoConfig {

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.database}")
    private String database;

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
        int availablePort = isMongoRunning() ? findAvailablePort() : port;
        mongoServer.bind(host, availablePort);
        return mongoServer;
    }

    private String appendDatabaseName(String baseConnection) {
        return String.format("%s/%s", baseConnection, database);
    }

    private boolean isMongoRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(port));
    }

    private int findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);

            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available Port: 10000 ~ 65535");
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
        }

        return !isEmpty(pidInfo.toString());
    }

}
