package com.gabia.weat.gcellexcelserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.minio.MinioClient;

@Configuration
public class MinioConfig {

	@Value("${minio.access.key}")
	private String accessKey;

	@Value("${minio.access.secret}")
	private String secretKey;

	@Value("${minio.url}")
	private String minioUrl;

	@Bean
	public MinioClient minioClient() {
		return new MinioClient.Builder()
			.credentials(accessKey, secretKey)
			.endpoint(minioUrl)
			.build();
	}

}
