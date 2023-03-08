package com.gabia.weat.gcellexcelserver.service;

import static org.mockito.BDDMockito.*;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;

@ExtendWith(MockitoExtension.class)
class MinioServiceTest {

	@Mock
	private MinioClient minioClient;
	@InjectMocks
	private MinioService minioService;

	@Test
	@DisplayName("미니오 서비스를 통해 업로드하면 미니오 클라이언트를 통해 업로드가 수행된다.")
	void uploadFileToMinio() throws Exception {
		// given
		ObjectWriteResponse writeResponseMock = mock(ObjectWriteResponse.class);
		given(minioClient.putObject(any())).willReturn(writeResponseMock);
		ReflectionTestUtils.setField(minioService, "excelBucketName", "test");

		// when
		File file = new File("foo.txt");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write("foo".getBytes());
		minioService.uploadFileWithDelete(file, "foo.txt");

		// then
		then(minioClient).should().putObject(any());
		fileOutputStream.close();
		file.delete();
	}

	@Test
	@DisplayName("미니오 서비스를 통해 목적 파일로 미니오 파일을 이동시킬 수 있다.")
	void moveFileFromMinio() throws Exception {
		// given
		GetObjectResponse getObjectResponse = mock(GetObjectResponse.class);
		Iterable iterable = mock(Iterable.class);
		given(minioClient.getObject(any())).willReturn(getObjectResponse);
		ReflectionTestUtils.setField(minioService, "csvBucketName", "test");

		// when
		File file = new File("foo.csv");
		minioService.moveFile("foo.csv", file);

		// then
		then(minioClient).should().getObject(any());
		file.delete();
	}
}
