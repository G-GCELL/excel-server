package com.gabia.weat.gcellexcelserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.gabia.weat.gcellexcelserver.parser.CustomExpressionParser;
import com.gabia.weat.gcellexcelserver.parser.SpELExpressionEnvParser;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ExpressionBeanParserConfig {

	private final Environment environment;

	@Bean
	public CustomExpressionParser expressionBeanParser(){
		return new SpELExpressionEnvParser(environment);
	}

}