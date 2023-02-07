package com.gabia.weat.gcellexcelserver.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExcelDataJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	public void insertExcelDataList(List<ExcelData> excelDataList) {

	}

}
