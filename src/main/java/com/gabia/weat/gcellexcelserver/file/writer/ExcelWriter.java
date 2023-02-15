package com.gabia.weat.gcellexcelserver.file.writer;

import com.gabia.weat.gcellexcelserver.domain.type.MessageType;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.ResultSetDto;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.CreateProgressMsgDto;
import com.gabia.weat.gcellexcelserver.dto.MsgMetaDto;
import com.gabia.weat.gcellexcelserver.service.producer.CreateProgressProducer;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExcelWriter {

    private final int MAX_ROWS = 1_048_576;
    private final CreateProgressProducer createProgressProducer;

    public File writeWithProgress(ResultSetDto resultSetDto, String fileName, MsgMetaDto dto) {
        validateResult(resultSetDto);
        File file = new File(fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ResultSet resultSet = resultSetDto.resultSet()) {
            Workbook workbook = new Workbook(fileOutputStream, "GCELL", null);
            String[] columnNames = getColumnNames(resultSet.getMetaData());
            int[] columnTypes = getColumnsTypes(resultSet.getMetaData());
            Worksheet[] worksheets = setSheetWithHeader(workbook, resultSetDto.resultSetCount(), columnNames);

            int standard = calculateStandard(resultSetDto.resultSetCount());
            int rowCount = 1;
            while (resultSet.next()) {
                if (rowCount % standard == 0 && rowCount / standard < 10) {
                    sendProgressRateMsg(dto, (rowCount / standard) * 10);
                }
                writeExcelRow(worksheets, rowCount++, resultSet, columnTypes);
            }
            sendProgressRateMsg(dto, 100);
            workbook.finish();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return file;
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
            // TODO: Global Exception 처리
            throw new RuntimeException();
        }
    }

    private int calculateStandard(Integer resultSetCount) {
        if (resultSetCount / 10 == 0) {
            return 1;
        }
        return resultSetCount / 10;
    }

    private Worksheet[] setSheetWithHeader(Workbook workbook, Integer resultSetCount, String[] columnNames) {
        Worksheet[] worksheets = new Worksheet[(resultSetCount / MAX_ROWS) + 1];
        for (int i = 0; i < worksheets.length; i++) {
            worksheets[i] = workbook.newWorksheet("sheet" + i);
            writeHeaderWithBold(worksheets[i], columnNames);
        }
        return worksheets;
    }

    private void writeHeaderWithBold(Worksheet worksheet, String[] columnNames) {
        for (int i = 0; i < columnNames.length; i++) {
            worksheet.style(0, i).bold().set();
            worksheet.value(0, i, columnNames[i]);
        }
    }

    private void writeExcelRow(Worksheet[] worksheets, int rowCount, ResultSet resultSet,
                                      int[] columnTypes) throws SQLException {
        int currentRow = rowCount % MAX_ROWS;
        currentRow = currentRow == 0 ? currentRow + 1 : currentRow;
        for (int i = 0; i < columnTypes.length; i++) {
            Worksheet worksheet = worksheets[rowCount / MAX_ROWS];
            switch (columnTypes[i]) {
                case ColumnType.BIGINT -> worksheet.value(currentRow, i, resultSet.getLong(i + 1));
                case ColumnType.VARCHAR, ColumnType.DATETIME -> worksheet.value(currentRow, i, resultSet.getString(i + 1));
                case ColumnType.DECIMAL ->
                    worksheet.value(currentRow, i, String.format("%.20f", resultSet.getBigDecimal(i + 1)));
            }
        }
    }

    private void sendProgressRateMsg(MsgMetaDto dto, int rate) {
        createProgressProducer.sendMessage(new CreateProgressMsgDto(
            dto.memberId(),
            MessageType.FILE_CREATION_PROGRESS,
            dto.fileName(),
            rate
        ));
    }

}
