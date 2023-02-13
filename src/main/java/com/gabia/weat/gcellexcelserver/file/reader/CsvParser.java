package com.gabia.weat.gcellexcelserver.file.reader;

import java.io.IOException;

public interface CsvParser {

    void insertWithCsv(String csvFilePath) throws IOException;

}
