package com.gabia.weat.gcellexcelserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitmqProperty {

	private String host;
	private int port;
	private String username;
	private String password;
	private ExchangeProperty exchange;
	private QueueProperty queue;
	private ListenerProperty listener;

	@Getter
	@AllArgsConstructor
	public static class ExchangeProperty {
		private String fileCreateProgressExchange;
	}

	@Getter
	@AllArgsConstructor
	public static class QueueProperty {
		private String fileCreateRequestQueue;
	}

	@Getter
	@AllArgsConstructor
	public static class ListenerProperty {
		private Integer concurrency;
		private Integer maxConcurrency;
		private Integer prefetch;
	}

}
