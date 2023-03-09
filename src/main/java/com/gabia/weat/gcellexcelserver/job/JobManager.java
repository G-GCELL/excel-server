package com.gabia.weat.gcellexcelserver.job;

import java.io.File;
import java.time.YearMonth;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.domain.type.JobType;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.dto.log.LogFormatFactory;
import com.gabia.weat.gcellexcelserver.repository.UserLevelLock;
import com.gabia.weat.gcellexcelserver.service.producer.CsvUpdateRequestProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JobManager {

	private final UserLevelLock userLevelLock;
	private final LogFormatFactory logFormatFactory;
	private final CsvUpdateRequestProducer csvUpdateRequestProducer;
	private final String ADD_JOB_LOCK_NAME = "add";
	private final String DATA_FILE_DIRECTORY = "data" + File.separator + "auto";

	@Scheduled(cron = "0 0 0 * * ?")
	public void addJobAutoTrigger() {
		userLevelLock.executeWithLock(this::addJobAuto, ADD_JOB_LOCK_NAME);
	}

	private void addJobAuto() {
		File[] files = new File(DATA_FILE_DIRECTORY).listFiles();
		if (files == null) {
			return;
		}
		logFormatFactory.startTrace();
		Arrays.stream(files).filter(FileValidator::isToUpdate).forEach(file -> {
			int baseNameInteger = Integer.parseInt(FilenameUtils.getBaseName(file.getName()));
			csvUpdateRequestProducer.sendMessage(MessageWrapperDto.wrapMessageDto(
				new CsvUpdateRequestDto(
					DATA_FILE_DIRECTORY + File.separator + file.getName(),
					YearMonth.of(baseNameInteger / 100, baseNameInteger % 100),
					JobType.AUTO), logFormatFactory.getTraceId()
			));
		});
	}

}
