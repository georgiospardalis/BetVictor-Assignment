package com.pardalis.betvictorassignment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;

@Configuration
public class MongoConfig {
    @Bean
    public MongoClientFactoryBean mongo() {
        MongoClientFactoryBean mongoClientFactoryBean =  new MongoClientFactoryBean();

        mongoClientFactoryBean.setHost("localhost");
        mongoClientFactoryBean.setPort(27017);

        return mongoClientFactoryBean;
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongo().getObject(), "");

        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);

        return mongoTemplate;
    }
}
