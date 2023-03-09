package com.gabia.weat.gcellexcelserver.service.producer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.gabia.weat.gcellexcelserver.domain.type.JobType;
import com.gabia.weat.gcellexcelserver.dto.MessageDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;

@ExtendWith(MockitoExtension.class)
class CsvUpdateRequestProducerTest {

	@Mock
	private RabbitTemplate csvUpdateRequestRabbitTemplate;
	@InjectMocks
	private CsvUpdateRequestProducer csvUpdateRequestProducer;

	@Test
	@DisplayName("엑셀_생성_실패_메시지_발행_테스트")
	public void sendMessage_test() {
		// given
		String traceId = "testid";
		MessageWrapperDto<MessageDto.CsvUpdateRequestDto> messageWrapperDto = MessageWrapperDto.wrapMessageDto(
			new MessageDto.CsvUpdateRequestDto("data/202202.csv", null, JobType.AUTO), traceId
		);

		// when && then
		assertThatCode(() -> csvUpdateRequestProducer.sendMessage(messageWrapperDto)).doesNotThrowAnyException();
		verify(csvUpdateRequestRabbitTemplate, times(1)).convertAndSend(eq(messageWrapperDto));
	}

}
