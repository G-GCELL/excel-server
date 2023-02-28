package com.gabia.weat.gcellexcelserver.job;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.YearMonth;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.annotation.JobLog;
import com.gabia.weat.gcellexcelserver.file.reader.CsvParser;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataUpdateJob {

	private final CsvParser csvParser;

	@JobLog(jobName = "update with file")
	public void updateWithFilePath(String path) throws SQLException, IOException {
		int baseName = Integer.parseInt(FilenameUtils.getBaseName(new File(path).getName()));
		csvParser.insertWithCsv(path, YearMonth.of(baseName / 100, baseName % 100));
	}

}
