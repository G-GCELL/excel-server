package com.gabia.weat.gcellexcelserver.dto.log;

import org.slf4j.event.Level;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TimerLogFormatDto extends LogFormatDto{

	String methodName;
	String input;
	boolean isStart;
	long time;

	@Builder
	public TimerLogFormatDto(Level level, String serverName, String traceId, String methodName, String input,
		boolean isStart, long time) {
		super(level, serverName, traceId);
		this.methodName = methodName;
		this.input = input;
		this.isStart = isStart;
		this.time = time;
	}

	@Override
	public String toString() {
		long second = time / 1000;
		long ms = time % 1000;

		return String.format("[%s] [%s] %s, time: %ds %dms type: %s input: %s",
			this.serverName,
			this.traceId,
			this.methodName,
			second,
			ms,
			isStart? "START" : "END",
			this.input
		);
	}

}