package com.gabia.weat.gcellexcelserver.job;

import java.io.File;
import java.time.YearMonth;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.annotation.JobLog;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellexcelserver.job.data.DataUpdateJob;
import com.gabia.weat.gcellexcelserver.job.data.FileValidator;
import com.gabia.weat.gcellexcelserver.job.structure.LimitedJobQueue;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JobManager {

	private final DataUpdateJob dataUpdateJob;
	private LimitedJobQueue queue = new LimitedJobQueue();
	private final String DATA_FILE_DIRECTORY = "data";

	@Scheduled(cron = "0 0 0 * * ?")
	public void addQueueAuto() {
		File[] files = new File(DATA_FILE_DIRECTORY).listFiles();
		if (files == null) {
			return;
		}
		Arrays.stream(files).filter(FileValidator::isToUpdate).forEach(file -> {
			int baseNameInteger = Integer.parseInt(FilenameUtils.getBaseName(file.getName()));
			queue.add(new CsvUpdateRequestDto(
				DATA_FILE_DIRECTORY + File.separator + file.getName(),
				YearMonth.of(baseNameInteger / 100, baseNameInteger % 100)
			));
		});
	}

	@JobLog(jobName = "update with queue")
	@Scheduled(cron = "0 15/30 * * * ?")
	public void updateExcelData() {
		while (!queue.isEmpty()) {
			CsvUpdateRequestDto dto = queue.poll();
			update(dto.filePath(), dto.deleteTarget());
		}
	}

	public void addQueueManual(@Valid CsvUpdateRequestDto csvUpdateRequestDto) {
		if (FileValidator.isCsvFile(new File(csvUpdateRequestDto.filePath()))) {
			queue.addManual(csvUpdateRequestDto);
		}
	}

	private void update(String filePath, YearMonth deleteTarget) {
		try {
			dataUpdateJob.updateWithFilePath(filePath, deleteTarget);
		} catch (Exception exception) {
			// Exception handling through AOP is performed
		}
	}

}
