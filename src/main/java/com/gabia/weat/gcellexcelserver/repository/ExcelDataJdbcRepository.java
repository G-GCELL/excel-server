package com.gabia.weat.gcellexcelserver.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExcelDataJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	public void insertExcelDataList(List<ExcelData> excelDataList) {
		jdbcTemplate.batchUpdate(
			"insert into excel_data (account_id, start_date, end_date, product_code, cost) values (?, ?, ?, ?, ?)",
			new BatchPreparedStatementSetter() {
				@Override
				public void setValues(@NotNull PreparedStatement ps, int index) throws SQLException {
					ExcelData excelData = excelDataList.get(index);
					ps.setString(1, excelData.getAccountId());
					ps.setTimestamp(2, Timestamp.valueOf(excelData.getStartDate()));
					ps.setTimestamp(3, Timestamp.valueOf(excelData.getEndDate()));
					ps.setString(4, excelData.getProductCode());
					ps.setBigDecimal(5, excelData.getCost());
				}

				@Override
				public int getBatchSize() {
					return excelDataList.size();
				}
			});
	}

}
