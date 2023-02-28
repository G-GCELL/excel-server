package com.gabia.weat.gcellexcelserver.job;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JobManager {

	private final DataUpdateJob dataUpdateJob;
	private final String DATA_FILE_DIRECTORY = "data";

	@Scheduled(cron = "0 0 0 * * ?")
	public void updateExcelData() {
		File[] files = new File(DATA_FILE_DIRECTORY).listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (isToUpdate(file)) {
				update(file.getName());
			}
		}
	}

	private boolean isToUpdate(File file) {
		String fileName = FilenameUtils.getBaseName(file.getName());
		if (!FilenameUtils.getExtension(file.getName()).equals("csv")) {
			return false;
		}
		if (fileName.length() != 6) {
			return false;
		}
		if (getLastModifiedDate(file).isBefore(LocalDateTime.now().minusDays(1))) {
			return false;
		}
		try {
			int yearAndMonth = Integer.parseInt(fileName);
			YearMonth.of(yearAndMonth / 100, yearAndMonth % 100);
		} catch (Exception exception) {
			return false;
		}
		return true;
	}

	private LocalDateTime getLastModifiedDate(File file) {
		String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified());
		return LocalDateTime.parse(format.replace(" ", "T"));
	}

	private void update(String fileName) {
		try {
			dataUpdateJob.updateWithFilePath(DATA_FILE_DIRECTORY + File.separator + fileName);
		} catch (Exception exception) {
			// Exception handling through AOP is performed
		}
	}

}
