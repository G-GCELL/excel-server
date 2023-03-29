package com.gabia.weat.gcellexcelserver.service.consumer;

import static com.gabia.weat.gcellcommonmodule.dto.MessageDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.service.ExcelDataService;

@ExtendWith(MockitoExtension.class)
public class FileCreateRequestConsumerTest {

	@Mock
	private ExcelDataService excelDataService;
	@InjectMocks
	private FileCreateRequestConsumer fileCreateRequestConsumer;

	@Test
	@DisplayName("엑셀_생성_요청_메시지_소비_테스트")
	public void receiveMessage_test() throws Exception {
		// given
		FileCreateRequestMsgDto fileCreateRequestMsgDto = this.getFileCreateRequestDto();
		String traceId = "testid";
		MessageWrapperDto<FileCreateRequestMsgDto> messageWrapperDto = MessageWrapperDto.wrapMessageDto(
			fileCreateRequestMsgDto, traceId);

		// when & then
		assertThatCode(
			() -> fileCreateRequestConsumer.receiveMessage(messageWrapperDto)).doesNotThrowAnyException();
		verify(excelDataService, times(1)).createExcelFile(eq(messageWrapperDto));
	}

	private FileCreateRequestMsgDto getFileCreateRequestDto() {
		return new FileCreateRequestMsgDto(
			1L,
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