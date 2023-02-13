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
import org.springframework.boot.test.context.SpringBootTest;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellexcelserver.service.ExcelDataService;
import com.rabbitmq.client.Channel;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CreateRequestConsumerTest {

	@Mock
	private Channel channel;
	@Mock
	private ExcelDataService excelDataService;
	@InjectMocks
	private CreateRequestConsumer createRequestConsumer;

	@Test
	@DisplayName("엑셀_생성_요청_메시지_소비_테스트")
	public void receiveMessage_test() throws IOException, SQLException {
		// given
		FileCreateRequestDto fileCreateRequestDto = this.getFileCreateRequestDto();
		long tag = 0L;

		// when & then
		assertThatCode(() -> createRequestConsumer.receiveMessage(fileCreateRequestDto, channel, tag)).doesNotThrowAnyException();
		verify(channel, times(1)).basicAck(eq(tag), anyBoolean());
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
			null
		);
	}

}