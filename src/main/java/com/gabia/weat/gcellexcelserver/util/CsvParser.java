package com.gabia.weat.gcellexcelserver.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CsvParser {

	public List<ExcelData> parseToExcelDataList(String csvFilePath) {
		return null;
	}

}
