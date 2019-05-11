package com.pardalis.betvictorassignment;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.ConnectionFactory;
import javax.jms.QueueConnectionFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class JmsConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(JmsConfig.class);

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        configurer.configure(factory, connectionFactory);
        factory.setTransactionManager(transactionManager());

        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JmsTransactionManager transactionManager = new JmsTransactionManager();

        transactionManager.setConnectionFactory(jmsConnectionFactory());

        return transactionManager;
    }

    @Bean
    public QueueConnectionFactory jmsConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        InputStream inputStream = getClass().getResourceAsStream("application.properties");
        Properties properties = new Properties();

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("Failed to read properties file for jmsConenction", e);

            System.exit(1);
        }

        connectionFactory.buildFromProperties(properties);

        return connectionFactory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        return converter;
    }
}