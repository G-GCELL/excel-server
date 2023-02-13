package com.gabia.weat.gcellexcelserver.file.reader;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;

import java.io.IOException;
import java.util.List;

public interface CsvParser {

    List<ExcelData> parseToExcelDataList(String csvFilePath) throws IOException;

}
