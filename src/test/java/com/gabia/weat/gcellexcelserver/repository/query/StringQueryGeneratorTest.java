package com.gabia.weat.gcellexcelserver.repository.query;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellexcelserver.repository.ExcelDataJdbcRepository;

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
			new String[] {"123456", "789123"},
			null,
			null,
			new String[] {"Amazon S3"},
			LocalDateTime.of(2022, 1, 1, 0, 0, 0),
			LocalDateTime.of(2022, 12, 31, 0, 0, 0),
			LocalDateTime.of(2022, 1, 1, 0, 0, 0),
			LocalDateTime.of(2022, 12, 31, 0, 0, 0),
			new BigDecimal("0.0"),
			new BigDecimal("99.0")
		);
	}
}
