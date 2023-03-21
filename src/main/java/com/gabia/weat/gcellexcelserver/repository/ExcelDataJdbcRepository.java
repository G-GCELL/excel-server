package com.gabia.weat.gcellexcelserver.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.gabia.weat.gcellexcelserver.annotation.TimerLog;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.QuerySetDto;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.ResultSetDto;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.FileCreateRequestMsgDto;
import com.gabia.weat.gcellexcelserver.error.ErrorCode;
import com.gabia.weat.gcellexcelserver.error.exception.CustomException;
import com.gabia.weat.gcellexcelserver.repository.query.QueryGenerator;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExcelDataJdbcRepository {

	private final JdbcTemplate jdbcTemplate;
	private final DataSourceProperties properties;
	private final QueryGenerator queryGenerator;

	public void insertExcelDataList(List<ExcelData> excelDataList) {
		if (excelDataList.size() == 0) {
			return;
		}
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

	public void deleteWithYearMonth(YearMonth target) {
		if (target == null) {
			return;
		}
		LocalDateTime from = LocalDateTime.of(target.getYear(), target.getMonth(), 1, 0, 0, 0);
		LocalDateTime to = LocalDateTime.of(target.getYear(), target.getMonth(), target.atEndOfMonth().getDayOfMonth(),
			23, 59, 59);
		String sql = "delete from excel_data where start_date between ? and ?";
		jdbcTemplate.update(sql, from, to);
	}

	public void optimization() {
		jdbcTemplate.update("set GLOBAL innodb_optimize_fulltext_only=ON");
		jdbcTemplate.query("optimize table excel_data", (RowMapper<String>)(rs, rowNum) -> null);
		jdbcTemplate.update("set GLOBAL innodb_optimize_fulltext_only=OFF");
	}

	@TimerLog
	public ResultSetDto getResultSet(FileCreateRequestMsgDto dto) throws SQLException {
		Connection conn = getConnection();
		validateColumnNames(dto.columnNames(), conn);

		QuerySetDto querySet = queryGenerator.generateCountQuery(dto);
		PreparedStatement statement = conn.prepareStatement(querySet.sql());
		for (Entry<Integer, Object> entry : querySet.params().entrySet()) {
			bindParam(statement, entry);
		}
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		int count = resultSet.getInt(1);

		querySet = queryGenerator.generateQuery(dto);
		statement = conn.prepareStatement(querySet.sql());
		for (Entry<Integer, Object> entry : querySet.params().entrySet()) {
			bindParam(statement, entry);
		}

		statement.setFetchSize(Integer.MIN_VALUE);
		resultSet = statement.executeQuery();

		return new ResultSetDto(resultSet, count);
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(properties.getUrl(), properties.getUsername(),
			properties.getPassword());
	}

	private void validateColumnNames(List<String> columnNames, Connection conn) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(queryGenerator.generateSingleResultQuery());
		ResultSet resultSet = statement.executeQuery();
		List<String> columnList = getColumnNames(resultSet.getMetaData());
		for (String columnName : columnNames) {
			if (!columnList.contains(columnName)) {
				throw new CustomException(ErrorCode.INVALID_COLUMN_NAME);
			}
		}
	}

	private void bindParam(PreparedStatement statement, Entry<Integer, Object> entry) throws SQLException {
		switch (entry.getValue().getClass().getSimpleName()) {
			case "String" -> statement.setString(entry.getKey(), (String)entry.getValue());
			case "LocalDateTime" -> statement.setTimestamp(
				entry.getKey(),
				Timestamp.valueOf((LocalDateTime)entry.getValue())
			);
			case "BigDecimal" -> statement.setBigDecimal(entry.getKey(), (BigDecimal)entry.getValue());
		}
	}

	private List<String> getColumnNames(ResultSetMetaData metaData) throws SQLException {
		int columnCount = metaData.getColumnCount();
		List<String> columnNames = new ArrayList<>(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			columnNames.add(metaData.getColumnName(i));
		}
		return columnNames;
	}

}
