package com.wegotoo.config.embedded;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Configuration
@RequiredArgsConstructor
@DependsOn("portInspector")
@Profile("local")
public class EmbeddedRedisConfig {

    private final RedisProperties redisProperties;

    private final PortInspector portInspector;

    private RedisServer redisServer;

    @PostConstruct
    private void start() throws IOException {
        int redisPort = portInspector.isPortRunning(redisProperties.getPort()) ? portInspector.findAvailablePort()
                : redisProperties.getPort();
        redisProperties.setPort(redisPort);

        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    private void stop() throws IOException {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

}
