package com.gabia.weat.gcellexcelserver.repository;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

	@DisplayName("엑셀 데이터 리스트를 전달하면 해당 값들을 bulk insert 한다.")
	@Test
	void excelDataBulkInsert() {
		// given
		List<ExcelData> excelDataList = List.of(
			generateExcelData(), generateExcelData(), generateExcelData()
		);

		// when
		excelDataJdbcRepository.insertExcelDataList(excelDataList);

		// then
		List<ExcelData> result = excelDataRepository.findAll();
		assertThat(result.size()).isEqualTo(3);
	}

	private ExcelData generateExcelData() {
		LocalDateTime now = LocalDateTime.now();
		return ExcelData.createWithoutId("111111", now, now, "Amazon s3",
			new BigDecimal("1.00000000000000000001"));
	}

}
