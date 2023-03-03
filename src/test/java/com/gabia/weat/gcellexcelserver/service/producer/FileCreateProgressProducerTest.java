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

import com.gabia.weat.gcellexcelserver.domain.type.MessageType;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.FileCreateProgressMsgDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;

@ExtendWith(MockitoExtension.class)
public class FileCreateProgressProducerTest {

	@Mock
	private RabbitTemplate rabbitTemplate;
	@InjectMocks
	private FileCreateProgressProducer fileCreateProgressProducer;

	@Test
	@DisplayName("엑셀_생성_진행률_메시지_발행_테스트")
	public void sendMessage_test() {
		// given
		FileCreateProgressMsgDto fileCreateProgressMsgDto = this.getCreateProgressMsgDto();
		String traceId = "testid";
		MessageWrapperDto<FileCreateProgressMsgDto> messageWrapperDto = MessageWrapperDto.wrapMessageDto(fileCreateProgressMsgDto, traceId);

		// when & then
		assertThatCode(() -> fileCreateProgressProducer.sendMessage(messageWrapperDto)).doesNotThrowAnyException();
		verify(rabbitTemplate, times(1)).correlationConvertAndSend(eq(messageWrapperDto), any(CorrelationData.class));
	}

	private FileCreateProgressMsgDto getCreateProgressMsgDto() {
		return new FileCreateProgressMsgDto(
			1L,
			MessageType.FILE_CREATION_PROGRESS,
			"testFileName",
			10
		);
	}

}
