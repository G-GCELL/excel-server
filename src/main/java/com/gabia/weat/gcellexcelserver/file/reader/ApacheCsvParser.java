package com.gabia.weat.gcellexcelserver.file.reader;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;
import com.gabia.weat.gcellexcelserver.repository.ExcelDataJdbcRepository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApacheCsvParser implements CsvParser {

    private final ExcelDataJdbcRepository excelDataJdbcRepository;

    @Override
    public void insertWithCsv(String csvFilePath) throws IOException {
        File csvFile = new File(csvFilePath);
        CSVFormat formatter = CSVFormat.EXCEL
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        CSVParser parser = formatter.parse(new BufferedReader(new FileReader(csvFile)));

        List<ExcelData> excelDataList = parser.getRecords()
            .stream()
            .map(this::recordToExcelData)
            .toList();
        excelDataJdbcRepository.insertExcelDataList(excelDataList);
    }

    private LocalDateTime dateTimeParser(String gtime) {
        return LocalDateTime.parse(gtime.substring(0, gtime.length() - 1));
    }

    private ExcelData recordToExcelData(CSVRecord record) {
        return ExcelData.createWithoutId(
                record.get(0),
                dateTimeParser(record.get(1)),
                dateTimeParser(record.get(2)),
                record.get(3),
                new BigDecimal(record.get(4)));
    }

}
