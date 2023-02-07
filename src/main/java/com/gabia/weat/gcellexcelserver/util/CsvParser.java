package com.gabia.weat.gcellexcelserver.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;

public class CsvParser {

	public static List<ExcelData> parseToExcelDataList(String csvFilePath) throws IOException {
		File csvFile = new File(csvFilePath);
		CSVFormat formatter = CSVFormat.EXCEL
			.builder()
			.setHeader()
			.setSkipHeaderRecord(true)
			.build();

		CSVParser parser = formatter.parse(new BufferedReader(new FileReader(csvFile)));

		return parser.getRecords()
			.stream()
			.map(CsvParser::recordToExcelData)
			.toList();
	}

	private static LocalDateTime dateTimeParser(String gtime) {
		return LocalDateTime.parse(gtime.substring(0, gtime.length() - 1));
	}

	private static ExcelData recordToExcelData(CSVRecord record) {
		int endo = 200;
		return ExcelData.createWithoutId(record.get(0),
			dateTimeParser(record.get(1)),
			dateTimeParser(record.get(2)),
			record.get(3),
			new BigDecimal(record.get(4)));
	}

}
