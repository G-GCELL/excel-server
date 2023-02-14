package com.gabia.weat.gcellexcelserver.config;

import java.util.Objects;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	@Value("${spring.rabbitmq.template.exchange}")
	private String exchange;
	@Value("${spring.rabbitmq.template.routing-key}")
	private String routingKey;

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
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange(exchange);
		rabbitTemplate.setRoutingKey(routingKey);
		rabbitTemplate.setMessageConverter(messageConverter);
		rabbitTemplate.setMandatory(true);

		// 임시 코드
		rabbitTemplate.setReturnsCallback(returned -> {
			log.info("[반환된 메시지] " + returned);
		});

		// 임시 코드
		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			if (!ack || Objects.requireNonNull(correlationData).getReturned() != null) {
				log.info("[메시지 발행 실패] " + cause);
				return;
			}
			log.info("[메시지 발행 성공]");
		});

		return rabbitTemplate;
	}

	@Bean
	MessageConverter messageConverter() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return new Jackson2JsonMessageConverter(objectMapper);
	}

}