package com.gabia.weat.gcellexcelserver.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.job.data.DataUpdateJob;
import com.gabia.weat.gcellexcelserver.repository.UserLevelLock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobManager {

	private final DataUpdateJob dataUpdateJob;
	private final UserLevelLock userLevelLock;
	private final String ADD_JOB_LOCK_NAME = "add";
	private final String UPDATE_JOB_LOCK_NAME = "update";

	@Scheduled(cron = "0 0 0 * * ?")
	public void addJobAuto() {
		userLevelLock.executeWithLock(this::addJobTrigger, ADD_JOB_LOCK_NAME);
	}

	@Scheduled(cron = "0 15/30 * * * ?")
	public void updateExcelData() {
		userLevelLock.executeWithLock(this::updateJobTrigger, UPDATE_JOB_LOCK_NAME);
	}

	private void addJobTrigger() {
		dataUpdateJob.addJob();
	}

	private void updateJobTrigger() {
		dataUpdateJob.updateJob();
	}

}
