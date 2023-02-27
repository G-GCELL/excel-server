package com.gabia.weat.gcellexcelserver.dto.log;

import java.time.LocalDateTime;

import org.slf4j.event.Level;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JobLogFormatDto extends LogFormatDto {

	private String jobName;

	@Builder
	public JobLogFormatDto(Level level, String serverName, String traceId, String jobName) {
		super(level, serverName, traceId);
		this.jobName = jobName;
	}

	@Override
	public String toString() {
		StringBuilder log = frontFormat();
		log.append(jobName);
		return log.toString();
	}

	private StringBuilder frontFormat() {
		return new StringBuilder(String.format("[%s] %s [%s] ",
			this.serverName,
			LocalDateTime.now(),
			this.traceId
		));
	}

}
