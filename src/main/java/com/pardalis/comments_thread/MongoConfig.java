package com.pardalis.comments_thread;

import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.WriteResultChecking;

@Configuration
@EnableConfigurationProperties(value = MongoProperties.class)
public class MongoConfig {
    @Bean
    public MongoDbFactory mongoDbFactory(MongoProperties mongoProperties) throws Exception {
        return new SimpleMongoDbFactory(
                new MongoClient(mongoProperties.getHost(), mongoProperties.getPort()),
                mongoProperties.getDatabase());
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoProperties mongoProperties) throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(mongoProperties));

        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);

        return mongoTemplate;
    }
}