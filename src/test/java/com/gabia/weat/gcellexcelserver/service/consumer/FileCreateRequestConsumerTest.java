package com.gabia.weat.gcellexcelserver.service.consumer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.weat.gcellexcelserver.dto.MessageDto.FileCreateRequestMsgDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.service.ExcelDataService;
import com.rabbitmq.client.Channel;

@ExtendWith(MockitoExtension.class)
public class FileCreateRequestConsumerTest {

	@Mock
	private Channel channel;
	@Mock
	private ExcelDataService excelDataService;
	@InjectMocks
	private FileCreateRequestConsumer fileCreateRequestConsumer;

	@Test
	@DisplayName("엑셀_생성_요청_메시지_소비_테스트")
	public void receiveMessage_test() throws IOException, SQLException {
		// given
		FileCreateRequestMsgDto fileCreateRequestMsgDto = this.getFileCreateRequestDto();
		String traceId = "testid";
		MessageWrapperDto<FileCreateRequestMsgDto> messageWrapperDto = MessageWrapperDto.wrapMessageDto(
			fileCreateRequestMsgDto, traceId);
		long tag = 0L;

		// when & then
		assertThatCode(
			() -> fileCreateRequestConsumer.receiveMessage(messageWrapperDto, channel, tag)).doesNotThrowAnyException();
		verify(excelDataService, times(1)).createExcelFile(eq(messageWrapperDto));
		verify(channel, times(1)).basicAck(eq(tag), anyBoolean());
	}

	private FileCreateRequestMsgDto getFileCreateRequestDto() {
		return new FileCreateRequestMsgDto(
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
