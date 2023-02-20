package com.gabia.weat.gcellexcelserver.file.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;
import com.gabia.weat.gcellexcelserver.repository.ExcelDataJdbcRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CsvParser {

	private final ExcelDataJdbcRepository excelDataJdbcRepository;
	private final int INSERT_UNIT_SIZE = 10_000;
	private final String[] HEADERS_ORDER_RULE = {"account_id", "usage_date", "product_code", "cost"};

	public void insertWithCsv(String csvFilePath) {
		try (FileInputStream fileInputStream = new FileInputStream(csvFilePath)) {
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			skipHeader(bufferedReader);

			String line;
			List<ExcelData> excelDataList = new ArrayList<>();
			while ((line = bufferedReader.readLine()) != null) {
				excelDataList.add(parseLine(line));
				if (excelDataList.size() % INSERT_UNIT_SIZE == 0) {
					excelDataJdbcRepository.insertExcelDataList(excelDataList);
					excelDataList = new ArrayList<>();
				}
			}
			excelDataJdbcRepository.insertExcelDataList(excelDataList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void skipHeader(BufferedReader bufferedReader) throws IOException {
		String line;
		if ((line = bufferedReader.readLine()) != null) {
			validateHeader(line);
		}
	}

	private void validateHeader(String headerLine) {
		if (!Arrays.equals(headerLine.split(","), HEADERS_ORDER_RULE)) {
			//TODO: Global Exception 처리
		}
	}

	private ExcelData parseLine(String line) {
		String[] dataFragments = line.split(",|/");
		return ExcelData.createWithoutId(
			dataFragments[0],
			LocalDateTime.parse(dataFragments[1]),
			LocalDateTime.parse(dataFragments[2]),
			dataFragments[3],
			new BigDecimal(dataFragments[4])
		);
	}

}
