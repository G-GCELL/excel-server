package com.gabia.weat.gcellexcelserver.config;

import java.util.Objects;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitmqConfig {

	@Value("${server.name}")
	private String serverName;
	private final RabbitmqProperty property;

	@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(property.getHost());
		connectionFactory.setPort(property.getPort());
		connectionFactory.setUsername(property.getUsername());
		connectionFactory.setPassword(property.getPassword());
		connectionFactory.setPublisherReturns(true);
		connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
		connectionFactory.setConnectionNameStrategy(connectionNameStrategy());
		return connectionFactory;
	}

	@Bean
	ConnectionNameStrategy connectionNameStrategy() {
		return connectionFactory -> serverName;
	}

	@Bean
	RabbitAdmin rabbitAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	SimpleRabbitListenerContainerFactory fileCreateRequestListenerFactory() {
		SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		listenerContainerFactory.setConnectionFactory(connectionFactory());
		listenerContainerFactory.setMessageConverter(messageConverter());
		listenerContainerFactory.setContainerCustomizer(
			container -> container.setQueueNames(property.getQueue().getFileCreateRequestQueue()));
		listenerContainerFactory.setConcurrentConsumers(property.getListener().getConcurrency());
		listenerContainerFactory.setMaxConcurrentConsumers(property.getListener().getMaxConcurrency());
		listenerContainerFactory.setPrefetchCount(property.getListener().getPrefetch());
		listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		return listenerContainerFactory;
	}

	@Bean
	RabbitTemplate fileCreateProgressRabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setExchange(property.getExchange().getFileCreateProgressExchange());
		rabbitTemplate.setMessageConverter(messageConverter());
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
