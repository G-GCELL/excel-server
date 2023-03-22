package com.gabia.weat.gcellexcelserver.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabia.weat.gcellexcelserver.message.MailMessage;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AlarmAspect {

	private final MailMessage mailMessage;

	@Around("@annotation(com.gabia.weat.gcellexcelserver.annotation.MailAlarm)")
	public Object mailAlarmAdvisor(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		String input = getInput(joinPoint);
		String email = getEmail(joinPoint);
		Object result;
		try {
			result = joinPoint.proceed();
		} catch (Exception e) {
			mailMessage.fail(email, methodName, input, e.getMessage());
			throw e;
		}
		mailMessage.success(email, methodName, input);
		return result;
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
			input.append(parseJson(objectMapper, args[index]));
			if (index != paramNames.length - 1)
				input.append(", ");
		}
		input.append("}");
		return input.toString();
	}

	private String getEmail(ProceedingJoinPoint joinPoint) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		CodeSignature methodSignature = (CodeSignature)joinPoint.getSignature();
		String[] paramNames = methodSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();

		for (int index = 0; index < paramNames.length; index++) {
			if (paramNames[index].equals("email")) {
				return args[index].toString();
			}
		}
		return null;
	}

	private String parseJson(ObjectMapper mapper, Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			return value.toString();
		}
	}

}
