package com.gabia.weat.gcellexcelserver.file;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.domain.type.JobType;
import com.gabia.weat.gcellexcelserver.service.MinioService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileBackupManager {

	private final MinioService minioService;

	public File backup(String fileLocate, JobType jobType) throws IOException {
		return switch (jobType) {
			case MANUAL -> backupWithMinio(fileLocate);
			case AUTO -> backupWithLocal(fileLocate);
		};
	}

	private File backupWithMinio(String fileLocate) {
		File file = new File(getDateDirectoryPath(LocalDate.now()) + File.separator + fileLocate);
		minioService.moveFile(fileLocate, file);
		return file;
	}

	private File backupWithLocal(String fileLocate) throws IOException {
		File srcFile = new File(fileLocate);
		File destFile = new File(getDateDirectoryPath(LocalDate.now()) + File.separator + getFileName(fileLocate));
		FileUtils.copyFile(srcFile, destFile);
		return destFile;
	}

	private String getDateDirectoryPath(LocalDate time) {
		return time.toString().replace("-", File.separator);
	}

	private String getFileName(String filePath) {
		return FilenameUtils.getBaseName(filePath) + FilenameUtils.getExtension(filePath);
	}

}
