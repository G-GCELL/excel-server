package com.gabia.weat.gcellexcelserver.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private String port;
	@Value("${redis.password}")
	private String password;

	private final String REDISSON_HOST_PREFIX = "redis://";

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + host + ":" + port).setPassword(password);
		return Redisson.create(config);
	}

}
