package com.gabia.weat.gcellexcelserver.file.reader;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ApacheCsvParser implements CsvParser {

    @Override
    public List<ExcelData> parseToExcelDataList(String csvFilePath) throws IOException {
        File csvFile = new File(csvFilePath);
        CSVFormat formatter = CSVFormat.EXCEL
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        CSVParser parser = formatter.parse(new BufferedReader(new FileReader(csvFile)));

        return parser.getRecords()
                .stream()
                .map(this::recordToExcelData)
                .toList();
    }

    private LocalDateTime dateTimeParser(String gtime) {
        return LocalDateTime.parse(gtime.substring(0, gtime.length() - 1));
    }

    private ExcelData recordToExcelData(CSVRecord record) {
        return ExcelData.createWithoutId(record.get(0),
                dateTimeParser(record.get(1)),
                dateTimeParser(record.get(2)),
                record.get(3),
                new BigDecimal(record.get(4)));
    }

}
