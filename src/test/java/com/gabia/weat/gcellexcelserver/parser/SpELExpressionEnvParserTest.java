package com.gabia.weat.gcellexcelserver.parser;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gabia.weat.gcellexcelserver.config.ExpressionBeanParserConfig;

@SpringBootTest(classes = ExpressionBeanParserConfig.class)
public class SpELExpressionEnvParserTest {

	@Autowired
	private CustomExpressionParser expressionBeanParser;

	@Test
	@DisplayName("SpEL 표현식 파싱 테스트")
	public void parse_test() {
		// given
		String spEL = "${rabbitmq.exchange.file-create-progress-exchange}";

		// when
		String value = (String) expressionBeanParser.parse(spEL);

		// then
		assertThat(value).isEqualTo("exchange");

	}

}