package com.gabia.weat.gcellexcelserver.service;

import java.io.File;
import java.sql.SQLException;

import com.gabia.weat.gcellexcelserver.file.writer.ExcelWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellexcelserver.dto.ResultSetDto;
import com.gabia.weat.gcellexcelserver.repository.ExcelDataJdbcRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExcelDataService {

	private final ExcelDataJdbcRepository excelDataJdbcRepository;
	private final MinioService minioService;
	private final ExcelWriter excelWriter;

	public void createExcelFile(FileCreateRequestDto dto) throws SQLException {
		ResultSetDto result = excelDataJdbcRepository.getResultSet(dto);
		File excelFile = excelWriter.write(result, Thread.currentThread().toString());
		minioService.uploadFileWithDelete(excelFile, dto.fileName());
	}

}
