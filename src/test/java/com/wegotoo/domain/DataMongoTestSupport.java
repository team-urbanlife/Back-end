package com.wegotoo.domain;

import com.wegotoo.config.embedded.EmbeddedMongoConfig;
import com.wegotoo.config.MongoConfig;
import com.wegotoo.config.embedded.PortInspector;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

@DataMongoTest
@Import({MongoConfig.class, EmbeddedMongoConfig.class, PortInspector.class})
public abstract class DataMongoTestSupport {
}
