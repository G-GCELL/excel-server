package com.gabia.weat.gcellexcelserver.service.consumer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.weat.gcellexcelserver.dto.MessageDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.service.ExcelDataService;

@ExtendWith(MockitoExtension.class)
class CsvUpdateRequestConsumerTest {

	@Mock
	private ExcelDataService excelDataService;
	@InjectMocks
	private CsvUpdateRequestConsumer csvUpdateRequestConsumer;

	@Test
	@DisplayName("CSV_업데이트_요청_메시지_소비_테스트")
	public void receiveMessage_test() throws Exception {
		// given
		String traceId = "testid";
		MessageWrapperDto<CsvUpdateRequestDto> messageWrapperDto = MessageWrapperDto.wrapMessageDto(
			new CsvUpdateRequestDto("data/202202.csv", null), traceId
		);

		// when & then
		assertThatCode(
			() -> csvUpdateRequestConsumer.receiveMessage(messageWrapperDto)).doesNotThrowAnyException();
		verify(excelDataService, times(1)).updateExcelData(eq(messageWrapperDto));
	}

}
