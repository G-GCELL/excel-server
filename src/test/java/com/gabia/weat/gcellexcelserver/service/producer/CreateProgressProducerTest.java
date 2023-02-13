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
import com.gabia.weat.gcellexcelserver.dto.MessageDto.CreateProgressMsgDto;

@ExtendWith(MockitoExtension.class)
public class CreateProgressProducerTest {

	@Mock
	private RabbitTemplate rabbitTemplate;
	@InjectMocks
	private CreateProgressProducer createProgressProducer;

	@Test
	@DisplayName("엑셀_생성_진행률_메시지_발행_테스트")
	public void sendMessage_test() {
		// given
		CreateProgressMsgDto createProgressMsgDto = this.getCreateProgressMsgDto();

		// when & then
		assertThatCode(() -> createProgressProducer.sendMessage(createProgressMsgDto)).doesNotThrowAnyException();
		verify(rabbitTemplate, times(1)).correlationConvertAndSend(eq(createProgressMsgDto), any(CorrelationData.class));
	}

	private CreateProgressMsgDto getCreateProgressMsgDto() {
		return new CreateProgressMsgDto(
			1L,
			MessageType.FILE_CREATION_PROGRESS,
			"testFileName",
			10
		);
	}

}
