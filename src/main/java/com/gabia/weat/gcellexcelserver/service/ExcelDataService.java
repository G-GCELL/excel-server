package com.gabia.weat.gcellexcelserver.service;

import java.io.File;
import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellexcelserver.dto.ResultSetDto;
import com.gabia.weat.gcellexcelserver.repository.ExcelDataJdbcRepository;
import com.gabia.weat.gcellexcelserver.util.ExcelWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExcelDataService {

	private final ExcelDataJdbcRepository excelDataJdbcRepository;
	private final MinioService minioService;

	public void createExcelFile(FileCreateRequestDto dto) throws SQLException {
		File file = new File(Thread.currentThread().toString());
		ResultSetDto result = excelDataJdbcRepository.getResultSet(dto);
		ExcelWriter.writeExcel(file, result);
		minioService.uploadFile(file, dto.fileName());
		file.delete();
	}

}
