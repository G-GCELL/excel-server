package com.gabia.weat.gcellexcelserver.aspect;

import com.gabia.weat.gcellexcelserver.annotation.ConsumerLog;
import com.gabia.weat.gcellexcelserver.annotation.ProducerLog;
import com.gabia.weat.gcellexcelserver.domain.type.TargetType;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.dto.log.MessageBrokerLogFormatDto.MessageBrokerLogFormatDtoBuilder;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabia.weat.gcellexcelserver.dto.log.LogFormatFactory;
import com.gabia.weat.gcellexcelserver.error.exception.CustomException;
import com.gabia.weat.gcellexcelserver.parser.CustomExpressionParser;
import com.gabia.weat.gcellexcelserver.service.log.LogPrinter;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

	private final LogFormatFactory logFormatFactory;
	private final LogPrinter logPrinter;
	private final CustomExpressionParser expressionBeanParser;

	@Around("@annotation(consumerLog)")
	public Object consumerLogAdvisor(ProceedingJoinPoint joinPoint, ConsumerLog consumerLog) throws Throwable {
		return this.messageLogAdviceLog(joinPoint, TargetType.CONSUMER, consumerLog.queue());
	}

	@Around("@annotation(producerLog)")
	public Object producerLogAdvisor(ProceedingJoinPoint joinPoint, ProducerLog producerLog) throws Throwable {
		return this.messageLogAdviceLog(joinPoint, TargetType.PRODUCER, producerLog.exchange());
	}

	private Object messageLogAdviceLog(ProceedingJoinPoint joinPoint, TargetType type, String target) throws Throwable {
		try {
			this.setTraceId(joinPoint);
			this.printMessageBrokerLog(type, target, this.getInput(joinPoint));
			return joinPoint.proceed();
		} catch (Exception e) {
			this.printErrorLog(e);
		}
		return null;
	}

	private void setTraceId(ProceedingJoinPoint joinPoint){
		Object[] args = joinPoint.getArgs();
		for (Object arg : args){
			if (arg instanceof MessageWrapperDto<?> dto){
				logFormatFactory.startTrace(dto.getTraceId());
				break;
			}
		}
	}

	private String getInput(ProceedingJoinPoint joinPoint) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		CodeSignature methodSignature = (CodeSignature)joinPoint.getSignature();
		String[] paramNames = methodSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();

		StringBuilder input = new StringBuilder("{");
		for (int index = 0; index < paramNames.length; index++) {
			input.append(paramNames[index]);
			input.append(" : ");
			input.append(this.parseJson(objectMapper, args[index]));
			if (index != paramNames.length - 1)
				input.append(", ");
		}
		input.append("}");
		return input.toString();
	}

	private String parseJson(ObjectMapper mapper, Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			return value.toString();
		}
	}

	private void printErrorLog(Exception e) {
		String message = e.getMessage();
		if (e instanceof CustomException ce){
			message = ce.getErrorCode().getMessage();
		}
		StackTraceElement stackTraceElement = e.getStackTrace()[0];
		logPrinter.print(logFormatFactory.getErrorLogFormatBuilder()
			.className(stackTraceElement.getClassName())
			.methodName(stackTraceElement.getMethodName())
			.exceptionName(e.getClass().getName())
			.message(message)
			.build());
	}

	private void printMessageBrokerLog(TargetType type, String target, String input) {
		String targetName = (String)expressionBeanParser.parse(target);
		MessageBrokerLogFormatDtoBuilder logFormatDtoBuilder = logFormatFactory.getMessageBrokerLogFormatBuilder()
			.type(type)
			.exchangeName(type == TargetType.PRODUCER ? targetName : null)
			.queueName(type == TargetType.CONSUMER ? targetName : null)
			.input(input);

		logPrinter.print(logFormatDtoBuilder.build());
	}

}