package com.gabia.weat.gcellexcelserver.file.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;
import com.gabia.weat.gcellexcelserver.repository.ExcelDataJdbcRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StreamCsvParser implements CsvParser {

	private final ExcelDataJdbcRepository excelDataJdbcRepository;
	private final int INSERT_UNIT_SIZE = 10_000;

	@Override
	public void insertWithCsv(String csvFilePath) throws IOException {
		try (FileInputStream fileInputStream = new FileInputStream(csvFilePath)) {
			Scanner scanner = new Scanner(fileInputStream);
			List<ExcelData> excelDataList = new ArrayList<>();

			skipHeader(scanner);
			while (scanner.hasNextLine()) {
				excelDataList.add(parseLine(scanner.nextLine()));
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

	private void skipHeader(Scanner scanner) {
		if (scanner.hasNextLine()) {
			scanner.nextLine();
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
