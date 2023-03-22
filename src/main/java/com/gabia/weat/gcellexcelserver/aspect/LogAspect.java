package com.gabia.weat.gcellexcelserver.aspect;

import com.gabia.weat.gcellexcelserver.annotation.ConsumerLog;
import com.gabia.weat.gcellexcelserver.annotation.ProducerLog;
import com.gabia.weat.gcellexcelserver.domain.type.TargetType;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.dto.log.LogFormatFactory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabia.weat.gcellexcelserver.service.log.LogPrinter;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

	private final LogFormatFactory logFormatFactory;
	private final LogPrinter logPrinter;

	@Around("@annotation(consumerLog)")
	public Object consumerLogAdvisor(ProceedingJoinPoint joinPoint, ConsumerLog consumerLog) throws Throwable {
		this.setTraceId(joinPoint);
		logPrinter.printMessageBrokerLog(TargetType.CONSUMER, consumerLog.queue(), this.getInput(joinPoint), null);
		return joinPoint.proceed();
	}

	@Around("@annotation(producerLog)")
	public Object producerLogAdvisor(ProceedingJoinPoint joinPoint, ProducerLog producerLog) throws Throwable {
		Exception exception = null;
		try {
			return joinPoint.proceed();
		} catch (Exception e) {
			exception = e;
			throw e;
		} finally {
			logPrinter.printMessageBrokerLog(
				TargetType.PRODUCER,
				producerLog.exchange(),
				this.getInput(joinPoint),
				exception
			);
		}
	}

	@Around("@annotation(com.gabia.weat.gcellexcelserver.annotation.TimerLog)")
	public Object timerLogAdvisor(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		String input = this.getInput(joinPoint);
		long startTime = System.currentTimeMillis();
		logPrinter.printTimerLog(methodName, input, true, 0);
		Object result = joinPoint.proceed();
		logPrinter.printTimerLog(methodName, input, false, System.currentTimeMillis() - startTime);
		return result;
	}

	private void setTraceId(ProceedingJoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof MessageWrapperDto<?> dto) {
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

}
