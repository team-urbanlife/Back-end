package com.wegotoo.domain;

import com.wegotoo.config.EmbeddedMongoConfig;
import com.wegotoo.config.MongoConfig;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

@DataMongoTest
@Import({MongoConfig.class, EmbeddedMongoConfig.class})
public abstract class DataMongoTestSupport {
}
