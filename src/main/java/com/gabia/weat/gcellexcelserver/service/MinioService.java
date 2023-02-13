package com.gabia.weat.gcellexcelserver.service;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MinioService {

	private final MinioClient minioClient;

	@Value("${minio.bucket.name}")
	private String bucketName;

	public void uploadFileWithDelete(File file, String fileName) {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			minioClient.putObject(PutObjectArgs.builder()
				.bucket(bucketName)
				.object(fileName)
				.stream(fileInputStream, file.length(), -1)
				.build()
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		file.delete();
	}

}
