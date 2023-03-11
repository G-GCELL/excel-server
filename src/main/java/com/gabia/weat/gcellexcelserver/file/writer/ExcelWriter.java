package com.gabia.weat.gcellexcelserver.file.writer;

import com.gabia.weat.gcellexcelserver.annotation.TimerLog;
import com.gabia.weat.gcellexcelserver.converter.MessageMetaDtoConverter;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.ResultSetDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.dto.MessageMetaDto;
import com.gabia.weat.gcellexcelserver.error.ErrorCode;
import com.gabia.weat.gcellexcelserver.error.exception.CustomException;
import com.gabia.weat.gcellexcelserver.service.producer.FileCreateProgressProducer;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExcelWriter {

    private final int MAX_RESULT_COUNT = 1_500_000;
    private final int MAX_ROWS = 1_048_576;
    private final int FLUSH_UNIT = 100;
    private final FileCreateProgressProducer fileCreateProgressProducer;

    @TimerLog
    public File writeWithProgress(ResultSetDto resultSetDto, String fileName, MessageMetaDto dto) {
        validateResult(resultSetDto);
        File file = new File(fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            writeExcel(resultSetDto, dto, fileOutputStream);
        } catch (Exception exception) {
            file.delete();
            throw new CustomException(exception, ErrorCode.EXCEL_WRITE_FAIL);
        }
        return file;
    }

    private void writeExcel(ResultSetDto resultSetDto, MessageMetaDto dto, FileOutputStream fileOutputStream)
        throws SQLException, IOException {
        ResultSet resultSet = resultSetDto.resultSet();
        Workbook workbook = new Workbook(fileOutputStream, "GCELL", null);
        String[] columnNames = getColumnNames(resultSet.getMetaData());
        int[] columnTypes = getColumnsTypes(resultSet.getMetaData());

        int sheetIndex = 0;
        Worksheet worksheet = setSheetWithHeader(workbook, columnNames, sheetIndex++);

        int standard = calculateStandard(resultSetDto.resultSetCount());
        int rowCount = 1;
        while (resultSet.next()) {
            if (rowCount % standard == 0 && rowCount / standard < 10) {
                sendProgressRateMsg(dto, (rowCount / standard) * 10);
            }
            if (rowCount % MAX_ROWS == 0) {
                worksheet.finish();
                worksheet = setSheetWithHeader(workbook, columnNames, sheetIndex++);
            }
            writeExcelRow(worksheet, rowCount++, resultSet, columnTypes);
        }
        sendProgressRateMsg(dto, 100);
        workbook.finish();
    }

    private String[] getColumnNames(ResultSetMetaData metaData) throws SQLException {
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }
        return columnNames;
    }

    private int[] getColumnsTypes(ResultSetMetaData metaData) throws SQLException {
        int columnCount = metaData.getColumnCount();
        int[] columnTypes = new int[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnTypes[i - 1] = metaData.getColumnType(i);
        }
        return columnTypes;
    }

    private void validateResult(ResultSetDto resultSetDto) {
        Integer resultCount = resultSetDto.resultSetCount();
        if (resultCount == null || resultCount <= 0) {
            throw new CustomException(ErrorCode.NO_RESULT);
        }
        if (resultCount > MAX_RESULT_COUNT) {
            throw new CustomException(ErrorCode.RESULT_COUNT_EXCEED);
        }
    }

    private int calculateStandard(Integer resultSetCount) {
        if (resultSetCount / 10 == 0) {
            return 1;
        }
        return resultSetCount / 10;
    }

    private Worksheet setSheetWithHeader(Workbook workbook, String[] columnNames, int sheetIndex) {
        Worksheet worksheet = workbook.newWorksheet("sheet" + sheetIndex);
        writeHeaderWithBold(worksheet, columnNames);
        return worksheet;
    }

    private void writeHeaderWithBold(Worksheet worksheet, String[] columnNames) {
        for (int i = 0; i < columnNames.length; i++) {
            worksheet.style(0, i).bold().set();
            worksheet.value(0, i, columnNames[i]);
        }
    }

    private void writeExcelRow(Worksheet worksheet, int rowCount, ResultSet resultSet,
        int[] columnTypes) throws SQLException, IOException {
        int currentRow = rowCount % MAX_ROWS;
        currentRow = currentRow == 0 ? currentRow + 1 : currentRow;
        for (int i = 0; i < columnTypes.length; i++) {
            switch (columnTypes[i]) {
                case ColumnType.BIGINT -> worksheet.value(currentRow, i, resultSet.getLong(i + 1));
                case ColumnType.VARCHAR, ColumnType.DATETIME -> worksheet.value(currentRow, i, resultSet.getString(i + 1));
                case ColumnType.DECIMAL ->
                    worksheet.value(currentRow, i, String.format("%.20f", resultSet.getBigDecimal(i + 1)));
            }
        }
        if (rowCount % FLUSH_UNIT == 0) {
            worksheet.flush();
        }
    }

    private void sendProgressRateMsg(MessageMetaDto dto, int rate) {
        fileCreateProgressProducer.sendMessage(
            MessageWrapperDto.wrapMessageDto(
                MessageMetaDtoConverter.toFileCreateProgressMsgDto(dto, rate), dto.traceId())
        );
    }

}
