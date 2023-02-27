package com.gabia.weat.gcellexcelserver.job;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.annotation.JobLog;
import com.gabia.weat.gcellexcelserver.file.reader.CsvParser;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataUpdateJob {

	private final CsvParser csvParser;

	@Scheduled(cron = "0 59 23 * * ?")
	@JobLog(jobName = "excel_data_daily_update")
	public void updateExcelData() throws SQLException, IOException {
		int yearMonth = Integer.parseInt(
			LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
		);
		csvParser.insertWithCsv("data" + File.separator + yearMonth + ".csv",
			YearMonth.of(yearMonth / 100, yearMonth % 100));
	}

	@JobLog(jobName = "excel_data_passive_update")
	public void updateExcelDataFixed(Integer yearMonth) throws SQLException, IOException {
		if (yearMonth == null) {
			return;
		}
		csvParser.insertWithCsv("data" + File.separator + yearMonth + ".csv",
			YearMonth.of(yearMonth / 100, yearMonth % 100));
	}

}
