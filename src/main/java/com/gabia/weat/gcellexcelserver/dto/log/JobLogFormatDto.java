package com.gabia.weat.gcellexcelserver.dto.log;

import java.time.LocalDateTime;

import org.slf4j.event.Level;

import com.gabia.weat.gcellexcelserver.domain.type.JobActionType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JobLogFormatDto extends LogFormatDto {

	private String jobName;
	private String input;
	private JobActionType action;

	@Builder
	public JobLogFormatDto(Level level, String serverName, String traceId, String jobName, String input,
		JobActionType action) {
		super(level, serverName, traceId);
		this.jobName = jobName;
		this.input = input;
		this.action = action;
	}

	@Override
	public String toString() {
		StringBuilder log = frontFormat();
		log.append(jobName).append(" ").append(input).append(" ").append(action);
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
