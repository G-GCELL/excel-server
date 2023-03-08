package com.gabia.weat.gcellexcelserver.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
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
import com.gabia.weat.gcellexcelserver.error.CustomRejectingErrorHandler;

import lombok.RequiredArgsConstructor;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitmqConfig {

	@Value("${server.name}")
	private String serverName;
	private final RabbitmqProperty property;
	private final CustomRejectingErrorHandler errorHandler;

	@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(property.getHost());
		connectionFactory.setPort(property.getPort());
		connectionFactory.setUsername(property.getUsername());
		connectionFactory.setPassword(property.getPassword());
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
	DirectExchange directExchange() {
		return new DirectExchange(property.getExchange().getDirectExchange(), true, false);
	}

	@Bean
	Queue fileCreateRequestQueue() {
		return new Queue(property.getQueue().getFileCreateRequestQueue(), true);
	}

	@Bean
	Queue csvUpdateRequestQueue() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("x-single-active-consumer", true);
		return new Queue(property.getQueue().getCsvUpdateRequestQueue(), true, false, false, arguments);
	}

	@Bean
	Declarables directExchangeBindings() {
		return new Declarables(
			BindingBuilder.bind(fileCreateRequestQueue())
				.to(directExchange())
				.with(property.getRoutingKey().getFileCreateRequestRoutingKey()),
			BindingBuilder.bind(csvUpdateRequestQueue())
				.to(directExchange())
				.with(property.getRoutingKey().getCsvUpdateRequestRoutingKey())
		);
	}

	@Bean
	SimpleRabbitListenerContainerFactory fileCreateRequestListenerFactory() {
		SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		listenerContainerFactory.setConnectionFactory(connectionFactory());
		listenerContainerFactory.setMessageConverter(messageConverter());
		listenerContainerFactory.setContainerCustomizer(
			container -> container.setQueueNames(property.getQueue().getFileCreateRequestQueue())
		);
		listenerContainerFactory.setConcurrentConsumers(property.getListener().getConcurrency());
		listenerContainerFactory.setMaxConcurrentConsumers(property.getListener().getMaxConcurrency());
		listenerContainerFactory.setPrefetchCount(property.getListener().getPrefetch());
		listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.AUTO);
		listenerContainerFactory.setErrorHandler(errorHandler);
		listenerContainerFactory.setDefaultRequeueRejected(false);
		return listenerContainerFactory;
	}

	@Bean
	SimpleRabbitListenerContainerFactory csvUpdateRequestListenerFactory() {
		SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		listenerContainerFactory.setConnectionFactory(connectionFactory());
		listenerContainerFactory.setMessageConverter(messageConverter());
		listenerContainerFactory.setContainerCustomizer(
			container -> container.setQueueNames(property.getQueue().getCsvUpdateRequestQueue())
		);
		listenerContainerFactory.setPrefetchCount(1);
		listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.AUTO);
		listenerContainerFactory.setErrorHandler(errorHandler);
		listenerContainerFactory.setDefaultRequeueRejected(false);
		return listenerContainerFactory;
	}

	@Bean
	RabbitTemplate fileCreateProgressRabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setExchange(property.getExchange().getFileCreateProgressExchange());
		rabbitTemplate.setMessageConverter(messageConverter());
		return rabbitTemplate;
	}

	@Bean
	RabbitTemplate csvUpdateRequestRabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setExchange(property.getExchange().getDirectExchange());
		rabbitTemplate.setRoutingKey(property.getRoutingKey().getCsvUpdateRequestRoutingKey());
		rabbitTemplate.setMessageConverter(messageConverter());
		return rabbitTemplate;
	}

	@Bean
	RabbitTemplate fileCreateErrorRabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setExchange(property.getExchange().getFileCreateErrorExchange());
		rabbitTemplate.setMessageConverter(messageConverter());
		return rabbitTemplate;
	}

	@Bean
	MessageConverter messageConverter() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return new Jackson2JsonMessageConverter(objectMapper);
	}

}
