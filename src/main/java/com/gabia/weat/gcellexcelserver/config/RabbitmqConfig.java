package com.gabia.weat.gcellexcelserver.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitmqConfig {

	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	@Value("${spring.rabbitmq.listener.simple.concurrency}")
	private Integer concurrency;
	@Value("${spring.rabbitmq.listener.simple.max-concurrency}")
	private Integer maxConcurrency;
	@Value("${spring.rabbitmq.listener.simple.prefetch}")
	private Integer prefetch;
	@Value("${spring.rabbitmq.listener.simple.acknowledge-mode}")
	private AcknowledgeMode acknowledgeMode;

	@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setPublisherReturns(true);
		connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
		return connectionFactory;
	}

	@Bean
	SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
		MessageConverter messageConverter) {
		SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		listenerContainerFactory.setConnectionFactory(connectionFactory);
		listenerContainerFactory.setMessageConverter(messageConverter);
		listenerContainerFactory.setConcurrentConsumers(concurrency);
		listenerContainerFactory.setMaxConcurrentConsumers(maxConcurrency);
		listenerContainerFactory.setPrefetchCount(prefetch);
		listenerContainerFactory.setAcknowledgeMode(acknowledgeMode);
		return listenerContainerFactory;
	}

	@Bean
	MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

}