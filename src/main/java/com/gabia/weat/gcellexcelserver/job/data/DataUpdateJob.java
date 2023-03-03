package com.gabia.weat.gcellexcelserver.job.data;

import java.io.IOException;
import java.sql.SQLException;
import java.time.YearMonth;

import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.annotation.JobLog;
import com.gabia.weat.gcellexcelserver.file.reader.CsvParser;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataUpdateJob {

	private final CsvParser csvParser;

	@JobLog(jobName = "update with file")
	public void updateWithFilePath(String path, YearMonth deleteTarget) throws SQLException, IOException {
		csvParser.insertWithCsv(path, deleteTarget);
	}

}
