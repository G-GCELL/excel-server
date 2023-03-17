package com.gabia.weat.gcellexcelserver.service.log;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.event.Level;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import com.gabia.weat.gcellexcelserver.domain.type.TargetType;
import com.gabia.weat.gcellexcelserver.dto.log.ErrorLogFormatDto;
import com.gabia.weat.gcellexcelserver.dto.log.LogFormatFactory;
import com.gabia.weat.gcellexcelserver.dto.log.MessageBrokerLogFormatDto;
import com.gabia.weat.gcellexcelserver.parser.CustomExpressionParser;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
public class ConsoleLogPrinterTest {

	@Mock
	private LogFormatFactory logFormatFactory;
	@Mock
	private CustomExpressionParser expressionParser;
	@InjectMocks
	private ConsoleLogPrinter logPrinter;

	@Test
	@DisplayName("에러_로그_테스트")
	public void printErrorLog_test(CapturedOutput output){
		// given
		Exception exception = new RuntimeException("test");
		given(logFormatFactory.getErrorLogFormatBuilder()).willReturn(this.getErrorLogFormatBuilder());

		// when
		logPrinter.printErrorLog(exception);

		// then
		assertThat(output.getOut().contains("message: test")).isTrue();
	}

	@Test
	@DisplayName("에러_로그_테스트")
	public void printMessageBrokerLog_test(CapturedOutput output){
		// given
		given(logFormatFactory.getMessageBrokerLogFormatBuilder()).willReturn(this.getMessageBrokerLogFormatBuilder());
		given(expressionParser.parse(any())).willReturn("testQueue");

		// when
		logPrinter.printMessageBrokerLog(TargetType.CONSUMER, "testQueue", "testInput", null);

		// then
		assertThat(output.getOut().contains("(testQueue)")).isTrue();
	}

	private ErrorLogFormatDto.ErrorLogFormatDtoBuilder getErrorLogFormatBuilder() {
		return ErrorLogFormatDto.builder()
			.level(Level.ERROR)
			.serverName("serverName")
			.traceId("testTraceId");
	}

	private MessageBrokerLogFormatDto.MessageBrokerLogFormatDtoBuilder getMessageBrokerLogFormatBuilder() {
		return MessageBrokerLogFormatDto.builder()
			.level(Level.INFO)
			.serverName("serverName")
			.traceId("testTraceId");
	}

}
