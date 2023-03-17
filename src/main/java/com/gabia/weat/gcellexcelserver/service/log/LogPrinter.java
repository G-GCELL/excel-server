package com.gabia.weat.gcellexcelserver.service.log;

import org.slf4j.event.Level;

import com.gabia.weat.gcellexcelserver.domain.type.TargetType;
import com.gabia.weat.gcellexcelserver.dto.log.LogFormatDto;
import com.gabia.weat.gcellexcelserver.dto.log.LogFormatFactory;
import com.gabia.weat.gcellexcelserver.dto.log.MessageBrokerLogFormatDto;
import com.gabia.weat.gcellexcelserver.dto.log.TimerLogFormatDto;
import com.gabia.weat.gcellexcelserver.error.exception.CustomException;
import com.gabia.weat.gcellexcelserver.parser.CustomExpressionParser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class LogPrinter {

	private final LogFormatFactory logFormatFactory;
	private final CustomExpressionParser expressionBeanParser;

	protected abstract void print(LogFormatDto logFormatDto);

	public void printErrorLog(Exception e) {
		String message = e.getMessage();
		if (e instanceof CustomException ce) {
			message = ce.getErrorCode().getMessage();
		}
		StackTraceElement stackTraceElement = e.getStackTrace()[0];
		this.print(logFormatFactory.getErrorLogFormatBuilder()
			.className(stackTraceElement.getClassName())
			.methodName(stackTraceElement.getMethodName())
			.exceptionName(e.getClass().getName())
			.message(message)
			.build());
	}

	public void printMessageBrokerLog(TargetType type, String target, String input, Exception exception) {
		String targetName = (String) expressionBeanParser.parse(target);
		MessageBrokerLogFormatDto.MessageBrokerLogFormatDtoBuilder logFormatDtoBuilder = logFormatFactory.getMessageBrokerLogFormatBuilder()
			.level(exception == null ? Level.INFO : Level.ERROR)
			.type(type)
			.exchangeName(type == TargetType.PRODUCER ? targetName : null)
			.queueName(type == TargetType.CONSUMER ? targetName : null)
			.exception(exception)
			.input(input);

		this.print(logFormatDtoBuilder.build());
	}

	public void printTimerLog(String methodName, String input, boolean isStart, long time){
		TimerLogFormatDto timerLogFormatDto = logFormatFactory.getTimerLogFormatBuilder()
			.methodName(methodName)
			.input(input)
			.isStart(isStart)
			.time(time)
			.build();

		this.print(timerLogFormatDto);
	}

}