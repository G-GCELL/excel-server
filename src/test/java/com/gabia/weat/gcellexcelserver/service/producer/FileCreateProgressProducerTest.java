package com.gabia.weat.gcellexcelserver.service.producer;

import static com.gabia.weat.gcellcommonmodule.dto.MessageDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;
import com.gabia.weat.gcellcommonmodule.type.MessageType;

@ExtendWith(MockitoExtension.class)
public class FileCreateProgressProducerTest {

	@Mock
	private RabbitTemplate fileCreateProgressRabbitTemplate;
	@InjectMocks
	private FileCreateProgressProducer fileCreateProgressProducer;

	@Test
	@DisplayName("엑셀_생성_진행률_메시지_발행_테스트")
	public void sendMessage_test() {
		// given
		FileCreateProgressMsgDto fileCreateProgressMsgDto = this.getCreateProgressMsgDto();
		String traceId = "testid";
		MessageWrapperDto<FileCreateProgressMsgDto> messageWrapperDto = MessageWrapperDto.wrapMessageDto(
			fileCreateProgressMsgDto, traceId);

		// when & then
		assertThatCode(() -> fileCreateProgressProducer.sendMessage(messageWrapperDto)).doesNotThrowAnyException();
		verify(fileCreateProgressRabbitTemplate, times(1)).convertAndSend(eq(messageWrapperDto));
	}

	private FileCreateProgressMsgDto getCreateProgressMsgDto() {
		return new FileCreateProgressMsgDto(
			1L,
			1L,
			MessageType.FILE_CREATION_PROGRESS,
			"testFileName",
			10
		);
	}

}