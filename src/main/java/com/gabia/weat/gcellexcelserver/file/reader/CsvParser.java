package com.gabia.weat.gcellexcelserver.file.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;
import com.gabia.weat.gcellexcelserver.repository.ExcelDataJdbcRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CsvParser {

	private final ExcelDataJdbcRepository excelDataJdbcRepository;
	private final int INSERT_UNIT_SIZE = 10_000;
	private final String[] HEADERS_ORDER_RULE = {"account_id", "usage_date", "product_code", "cost"};
	private final DataSource dataSource;

	public void insertWithCsv(String csvFilePath) {
		File copied = copyOriginCsv(csvFilePath);
		TransactionSynchronizationManager.initSynchronization();
		Connection connection = DataSourceUtils.getConnection(dataSource);

		try (FileInputStream fileInputStream = new FileInputStream(copied)) {
			connection.setAutoCommit(false);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			skipHeader(bufferedReader);

			String line;
			List<ExcelData> excelDataList = new ArrayList<>(INSERT_UNIT_SIZE);
			while ((line = bufferedReader.readLine()) != null) {
				excelDataList.add(parseLine(line));
				if (excelDataList.size() % INSERT_UNIT_SIZE == 0) {
					excelDataJdbcRepository.insertExcelDataList(excelDataList);
					excelDataList = new ArrayList<>(INSERT_UNIT_SIZE);
				}
			}
			excelDataJdbcRepository.insertExcelDataList(excelDataList);
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DataSourceUtils.releaseConnection(connection, dataSource);
			TransactionSynchronizationManager.unbindResource(dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}
	}

	private File copyOriginCsv(String csvFilePath) {
		String destPath = getDayDirectory() + "/" + csvFilePath;
		File srcFile = new File(csvFilePath);
		File destFile = new File(destPath);
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return destFile;
	}

	private String getDayDirectory() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/M/d"));
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
