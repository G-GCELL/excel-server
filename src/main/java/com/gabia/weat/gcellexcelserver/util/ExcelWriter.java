package com.gabia.weat.gcellexcelserver.util;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

import com.gabia.weat.gcellexcelserver.dto.ResultSetDto;

public class ExcelWriter {

	private static final String APPLICATION_NAME = "GCELL";
	private static final String APPLICATION_VERSION = "1.0";
	private static final int MAX_ROWS = 1_048_576;

	public static void writeExcel(File file, ResultSetDto resultSetDto) {
		validateResult(resultSetDto);
		try (FileOutputStream fileOutputStream = new FileOutputStream(file);
			ResultSet resultSet = resultSetDto.resultSet()) {
			Workbook workbook = new Workbook(fileOutputStream, APPLICATION_NAME, APPLICATION_VERSION);
			String[] excelColumns = getExcelColumns(resultSet.getMetaData());
			Worksheet[] worksheets = setSheetWithHeader(workbook, resultSetDto.resultSetCount(), excelColumns);

			int rowCount = 1;
			while (resultSet.next()) {
				writeExcelRow(worksheets, rowCount++, resultSet, excelColumns);
			}
			workbook.finish();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private static void validateResult(ResultSetDto resultSetDto) {
		Integer resultCount = resultSetDto.resultSetCount();
		if (resultCount == null || resultCount <= 0) {
			throw new RuntimeException();
		}
	}

	private static String[] getExcelColumns(ResultSetMetaData metaData) throws SQLException {
		String[] excelColumns = new String[metaData.getColumnCount()];
		for (int i = 1; i <= excelColumns.length; i++) {
			excelColumns[i - 1] = metaData.getColumnName(i);
		}
		return excelColumns;
	}

	private static Worksheet[] setSheetWithHeader(Workbook workbook, Integer resultSetCount, String[] excelColumns) {
		Worksheet[] worksheets = new Worksheet[(resultSetCount / MAX_ROWS) + 1];
		for (int i = 0; i < worksheets.length; i++) {
			worksheets[i] = workbook.newWorksheet("sheet" + i);
			writeHeaderWithBold(worksheets[i], excelColumns);
		}
		return worksheets;
	}

	private static void writeHeaderWithBold(Worksheet worksheet, String[] excelColumns) {
		for (int i = 0; i < excelColumns.length; i++) {
			worksheet.style(0, i).bold().set();
			worksheet.value(0, i, excelColumns[i]);
		}
	}

	private static void writeExcelRow(Worksheet[] worksheets, int rowCount, ResultSet resultSet,
		String[] excelColumns) throws SQLException {
		int currentRow = rowCount % MAX_ROWS;
		currentRow = currentRow == 0 ? currentRow + 1 : currentRow;
		for (int i = 0; i < excelColumns.length; i++) {
			worksheets[rowCount / MAX_ROWS].value(currentRow, i, resultSet.getString(excelColumns[i]));
		}
	}

}
