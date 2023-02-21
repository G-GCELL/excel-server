package com.gabia.weat.gcellexcelserver.service;

import static org.mockito.BDDMockito.*;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.ResultSetDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.file.writer.ExcelWriter;
import com.gabia.weat.gcellexcelserver.repository.ExcelDataJdbcRepository;
import com.gabia.weat.gcellexcelserver.service.producer.FileCreateProgressProducer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
class ExcelDataServiceTest {

	@Mock
	private MinioService minioService;
	@Mock
	private ExcelDataJdbcRepository excelDataJdbcRepository;
	@Mock
	private ExcelWriter excelWriter;
	@Mock
	private FileCreateProgressProducer fileCreateProgressProducer;
	@InjectMocks
	private ExcelDataService excelDataService;

	@Test
	@DisplayName("전달받은 조건으로부터 조회한 데이터를 이용해 엑셀 파일 생성 요청을 수행해야 한다.")
	void createExcelFileTest() throws SQLException {
		// Given
		ResultSetDto resultSetDto = new ResultSetDto(null, 10);
		String traceId = "testid";
		MessageWrapperDto<FileCreateRequestDto> messageWrapperDto = MessageWrapperDto.wrapMessageDto(
			getFileCreateRequestDto(), traceId);
		given(excelDataJdbcRepository.getResultSet(getFileCreateRequestDto())).willReturn(resultSetDto);

		// When
		excelDataService.createExcelFile(messageWrapperDto);

		// Then
		verify(excelDataJdbcRepository, times(1)).getResultSet(any());
		verify(excelWriter, times(1)).writeWithProgress(any(), any(), any());
	}

	private FileCreateRequestDto getFileCreateRequestDto() {
		return new FileCreateRequestDto(
			1L,
			"testFileName",
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null
		);
	}

}
