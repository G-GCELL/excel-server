package com.gabia.weat.gcellexcelserver.file.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;
import com.gabia.weat.gcellexcelserver.error.ErrorCode;
import com.gabia.weat.gcellexcelserver.repository.ExcelDataJdbcRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CsvParser {

	private final ExcelDataJdbcRepository excelDataJdbcRepository;
	private final int INSERT_UNIT_SIZE = 10_000;
	private final String[] HEADERS_ORDER_RULE = {"account_id", "usage_date", "product_code", "cost"};
	private final DataSource dataSource;

	public void insertWithCsv(File csvFile, YearMonth deleteTarget)
		throws SQLException, IOException {
		TransactionSynchronizationManager.initSynchronization();
		Connection connection = DataSourceUtils.getConnection(dataSource);

		try (FileInputStream fileInputStream = new FileInputStream(csvFile)) {
			connection.setAutoCommit(false);

			excelDataJdbcRepository.deleteWithYearMonth(deleteTarget);

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
			excelDataJdbcRepository.optimization();
			connection.commit();
		} catch (Exception exception) {
			connection.rollback();
			throw exception;
		} finally {
			DataSourceUtils.releaseConnection(connection, dataSource);
			TransactionSynchronizationManager.unbindResource(dataSource);
			TransactionSynchronizationManager.clearSynchronization();
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
			throw new IllegalArgumentException(ErrorCode.INVALID_CSV_HEADER.getMessage());
		}
	}

	private ExcelData parseLine(String line) {
		String[] dataFragments = line.split(",|/");
		if (dataFragments.length != HEADERS_ORDER_RULE.length + 1) {
			throw new IllegalArgumentException(ErrorCode.INVALID_CSV_FORMAT.getMessage());
		}
		return ExcelData.createWithoutId(
			dataFragments[0],
			parseDateTime(dataFragments[1]),
			parseDateTime(dataFragments[2]),
			dataFragments[3],
			new BigDecimal(dataFragments[4])
		);
	}

	private LocalDateTime parseDateTime(String dateTime) {
		if (dateTime.charAt(dateTime.length() - 1) == 'Z') {
			return LocalDateTime.parse(dateTime.substring(0, dateTime.length() - 1));
		}
		return LocalDateTime.parse(dateTime);
	}

}
