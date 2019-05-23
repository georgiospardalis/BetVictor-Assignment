package com.pardalis.betvictorassignment;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.QueueConnectionFactory;
import java.util.Properties;

@Configuration
@EnableJms
public class JmsConfig {
    @Resource
    private Properties properties;

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setTransactionManager(transactionManager());
        configurer.configure(factory, connectionFactory);

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

        connectionFactory.buildFromProperties(properties);
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy());

        return connectionFactory;
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();

        redeliveryPolicy.setMaximumRedeliveries(5);
        redeliveryPolicy.setRedeliveryDelay(1000);

        return redeliveryPolicy;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        return converter;
    }
}