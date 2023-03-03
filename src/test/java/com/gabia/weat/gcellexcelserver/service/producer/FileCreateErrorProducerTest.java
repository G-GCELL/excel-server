package com.gabia.weat.gcellexcelserver.service.producer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.gabia.weat.gcellexcelserver.dto.MessageDto.FileCreateErrorMsgDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;

@ExtendWith(MockitoExtension.class)
public class FileCreateErrorProducerTest {

	@Mock
	private RabbitTemplate fileCreateErrorRabbitTemplate;
	@InjectMocks
	private FileCreateErrorProducer fileCreateErrorProducer;

	@Test
	@DisplayName("엑셀_생성_실패_메시지_발행_테스트")
	public void sendMessage_test() {
		// given
		FileCreateErrorMsgDto fileCreateErrorMsgDto = this.getFileCreateErrorMsgDto();
		String traceId = "testid";
		MessageWrapperDto<FileCreateErrorMsgDto> messageWrapperDto = MessageWrapperDto.wrapMessageDto(
			fileCreateErrorMsgDto, traceId);

		// when && then
		assertThatCode(() -> fileCreateErrorProducer.sendMessage(messageWrapperDto)).doesNotThrowAnyException();
		verify(fileCreateErrorRabbitTemplate, times(1)).correlationConvertAndSend(eq(messageWrapperDto), any(
			CorrelationData.class));
	}

	private FileCreateErrorMsgDto getFileCreateErrorMsgDto() {
		return new FileCreateErrorMsgDto(
			1L,
			1L,
			400,
			"error message"
		);
	}

}
