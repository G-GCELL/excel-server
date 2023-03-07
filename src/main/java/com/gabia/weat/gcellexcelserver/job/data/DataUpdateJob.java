package com.gabia.weat.gcellexcelserver.job.data;

import java.io.File;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.annotation.JobLog;
import com.gabia.weat.gcellexcelserver.domain.UpdateJob;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellexcelserver.file.reader.CsvParser;
import com.gabia.weat.gcellexcelserver.service.UpdateJobService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataUpdateJob {

	private final CsvParser csvParser;
	private final UpdateJobService updateJobService;
	private final String DATA_FILE_DIRECTORY = "data";

	@JobLog(jobName = "Add Daily Jobs")
	public void addJob() {
		File[] files = new File(DATA_FILE_DIRECTORY).listFiles();
		if (files == null) {
			return;
		}
		Arrays.stream(files).filter(FileValidator::isToUpdate).forEach(file -> {
			int baseNameInteger = Integer.parseInt(FilenameUtils.getBaseName(file.getName()));
			updateJobService.addJob(new CsvUpdateRequestDto(
				DATA_FILE_DIRECTORY + File.separator + file.getName(),
				YearMonth.of(baseNameInteger / 100, baseNameInteger % 100)
			));
		});
	}

	@JobLog(jobName = "Single Add Job")
	public void addJobManual(@Valid CsvUpdateRequestDto csvUpdateRequestDto) {
		if (FileValidator.isCsvFile(new File(csvUpdateRequestDto.filePath()))) {
			updateJobService.addJobWithLimit(csvUpdateRequestDto);
		}
	}

	@JobLog(jobName = "Batch Update Jobs")
	public void updateJob() {
		List<UpdateJob> allJob = updateJobService.getAllJob();
		for (UpdateJob updateJob : allJob) {
			unitUpdateJob(updateJob);
		}
	}

	private void unitUpdateJob(UpdateJob job) {
		try {
			YearMonth deleteTarget = null;
			if (job.getTargetYear() != null && job.getTargetMonth() != null) {
				deleteTarget = YearMonth.of(job.getTargetYear(), job.getTargetMonth());
			}
			csvParser.insertWithCsv(job.getFilePath(), deleteTarget);
		} catch (Exception exception) {
			// Exception handling performed by AOP
		} finally {
			updateJobService.finishJob(job.getJobId());
		}
	}

}
