package com.gabia.weat.gcellexcelserver.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellcommonmodule.annotation.TimerLog;
import com.gabia.weat.gcellcommonmodule.error.ErrorCode;
import com.gabia.weat.gcellcommonmodule.error.exception.CustomException;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MinioService {

	private final MinioClient minioClient;

	@Value("${minio.bucket.excel}")
	private String excelBucketName;
	@Value("${minio.bucket.csv}")
	private String csvBucketName;

	@TimerLog
	public void uploadFileWithDelete(File file, String fileName) {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			minioClient.putObject(PutObjectArgs.builder()
				.bucket(excelBucketName)
				.object(fileName)
				.stream(fileInputStream, file.length(), -1)
				.build()
			);
		} catch (Exception exception) {
			throw new CustomException(exception, ErrorCode.MINIO_UPLOAD_FAIL);
		} finally {
			file.delete();
		}
	}

	public File downloadFile(String srcFileName) {
		File file = new File(srcFileName);
		try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			minioClient.getObject(
				GetObjectArgs.builder()
					.bucket(csvBucketName).object(srcFileName).build()
			).transferTo(fileOutputStream);
		} catch (Exception exception) {
			throw new CustomException(exception, ErrorCode.MINIO_DOWNLOAD_FAIL);
		}
		return file;
	}

}