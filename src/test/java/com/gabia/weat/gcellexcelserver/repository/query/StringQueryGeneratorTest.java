package com.gabia.weat.gcellexcelserver.repository.query;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

class StringQueryGeneratorTest {

	private StringQueryGenerator stringQueryGenerator = new StringQueryGenerator();

	@Test
	void name() throws SQLException {
		stringQueryGenerator.generateCountQuery(getDto());
	}

	private FileCreateRequestDto getDto() {
		return new FileCreateRequestDto(
			2L,
			"hello.xlsx",
			List.of("account_id"),
			List.of("123456", "789123"),
			null,
			null,
			List.of("amazonS3"),
			LocalDateTime.of(2022, 1, 1, 0, 0, 0),
			LocalDateTime.of(2022, 12, 31, 0, 0, 0),
			LocalDateTime.of(2022, 1, 1, 0, 0, 0),
			LocalDateTime.of(2022, 12, 31, 0, 0, 0),
			new BigDecimal("0.0"),
			new BigDecimal("99.0")
		);
	}
}
