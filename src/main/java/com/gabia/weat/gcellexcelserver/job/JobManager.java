package com.gabia.weat.gcellexcelserver.job;

import java.io.File;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.annotation.JobLog;
import com.gabia.weat.gcellexcelserver.domain.UpdateJob;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellexcelserver.job.data.DataUpdateJob;
import com.gabia.weat.gcellexcelserver.job.data.FileValidator;
import com.gabia.weat.gcellexcelserver.service.UpdateJobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JobManager {

	private final RedissonClient redissonClient;
	private final DataUpdateJob dataUpdateJob;
	private final UpdateJobService updateJobService;
	private final String DATA_FILE_DIRECTORY = "data";
	private final String ADD_JOB_LOCK_NAME = "add";
	private final String UPDATE_JOB_LOCK_NAME = "update";

	@Scheduled(cron = "0 0 0 * * ?")
	public void addJobAuto() {
		RLock lock = redissonClient.getLock(ADD_JOB_LOCK_NAME);
		try {
			if (lock.tryLock()) {
				addJob();
			}
		} finally {
			try {
				lock.unlock();
			} catch (Exception e) {
				// If the lock is not being acquired or the connection is lost while acquiring
			}
		}
	}

	private void addJob() {
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

	@Scheduled(cron = "0 15/30 * * * ?")
	@JobLog(jobName = "update with job queue")
	public void updateExcelData() throws InterruptedException {
		RLock lock = redissonClient.getLock(UPDATE_JOB_LOCK_NAME);
		if (lock.tryLock()) {
			List<UpdateJob> allJob = updateJobService.getAllJob();
			for (UpdateJob updateJob : allJob) {
				update(updateJob);
			}
		}
		try {
			lock.unlock();
		} catch (Exception e) {
			// If the lock is not being acquired or the connection is lost while acquiring
		}

	}

	public void addJobManual(@Valid CsvUpdateRequestDto csvUpdateRequestDto) {
		if (FileValidator.isCsvFile(new File(csvUpdateRequestDto.filePath()))) {
			updateJobService.addJobWithLimit(csvUpdateRequestDto);
		}
	}

	private void update(UpdateJob job) {
		try {
			YearMonth deleteTarget = null;
			if (job.getTargetYear() != null && job.getTargetMonth() != null) {
				deleteTarget = YearMonth.of(job.getTargetYear(), job.getTargetMonth());
			}
			dataUpdateJob.updateWithFilePath(job.getFilePath(), deleteTarget);
		} catch (Exception exception) {
			// Exception handling performed by AOP
		} finally {
			updateJobService.finishJob(job.getJobId());
		}
	}

}
