package com.gabia.weat.gcellexcelserver.repository;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;

@SpringBootTest
class ExcelDataJdbcRepositoryTest {

	@Autowired
	private ExcelDataRepository excelDataRepository;

	@Autowired
	private ExcelDataJdbcRepository excelDataJdbcRepository;

	@DisplayName("엑셀 데이터 다량 삽입 테스트")
	@Test
	void excelDataBulkInsert() {
		// given
		List<ExcelData> excelDataList = List.of(
			generateExcelData(), generateExcelData(), generateExcelData()
		);
		excelDataJdbcRepository.insertExcelDataList(excelDataList);

		// when
		List<ExcelData> result = excelDataRepository.findAll();

		// then
		assertThat(result.size()).isEqualTo(3);
	}

	private ExcelData generateExcelData() {
		return ExcelData.createWithoutId("111111", LocalDateTime.now(), LocalDateTime.now(), "Amazon s3",
			new BigDecimal("1.00000000000000000001"));
	}
}
