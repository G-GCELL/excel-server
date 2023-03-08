package com.gabia.weat.gcellexcelserver.job;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.apache.commons.io.FilenameUtils;

public class FileValidator {

	public static boolean isToUpdate(File file) {
		String fileName = FilenameUtils.getBaseName(file.getName());
		if (!isCsvFile(file) || fileName.length() != 6) {
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

	public static boolean isCsvFile(File file) {
		return FilenameUtils.getExtension(file.getName()).equals("csv");
	}

	private static LocalDateTime getLastModifiedDate(File file) {
		return LocalDateTime.parse(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(file.lastModified()));
	}

}
