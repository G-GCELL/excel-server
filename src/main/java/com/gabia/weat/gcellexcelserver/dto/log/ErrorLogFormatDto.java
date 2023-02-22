package com.gabia.weat.gcellexcelserver.dto.log;

import java.time.LocalDateTime;

import org.slf4j.event.Level;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorLogFormatDto extends LogFormatDto {

	private String className;
	private String methodName;
	private String exceptionName;
	private String message;

	@Builder
	public ErrorLogFormatDto(Level level, String serverName, String traceId, String className,
		String methodName, String exceptionName, String message) {
		super(level, serverName, traceId);
		this.className = className;
		this.methodName = methodName;
		this.exceptionName = exceptionName;
		this.message = message;
	}

	@Override
	public String toString() {
		return String.format("[%s] %s [%s] %s-%s, cause: %s, message: %s",
			this.serverName,
			LocalDateTime.now(),
			this.traceId,
			this.className,
			this.methodName,
			this.exceptionName,
			this.message
		);
	}

}