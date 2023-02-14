package com.gabia.weat.gcellexcelserver.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;

import com.gabia.weat.gcellexcelserver.dto.JdbcDto.QuerySetDto;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.ResultSetDto;
import com.gabia.weat.gcellexcelserver.repository.query.QueryGenerator;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;
import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;

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

	public ResultSetDto getResultSet(FileCreateRequestDto dto) throws SQLException {
		return new ResultSetDto(getResult(dto), getResultCount(dto));
	}

	public Integer getResultCount(FileCreateRequestDto dto) throws SQLException {
		Connection conn = getConnection();

		QuerySetDto querySet = queryGenerator.generateCountQuery(dto);
		PreparedStatement statement = conn.prepareStatement(querySet.sql());
		for (Entry<Integer, Object> entry : querySet.params().entrySet()) {
			bindParam(statement, entry);
		}

		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return resultSet.getInt(1);
	}

	public ResultSet getResult(FileCreateRequestDto dto) throws SQLException {
		Connection conn = getConnection();

		QuerySetDto querySet = queryGenerator.generateQuery(dto);
		PreparedStatement statement = conn.prepareStatement(querySet.sql());
		for (Entry<Integer, Object> entry : querySet.params().entrySet()) {
			bindParam(statement, entry);
		}

		statement.setFetchSize(Integer.MIN_VALUE);
		return statement.executeQuery();
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(properties.getUrl(), properties.getUsername(),
			properties.getPassword());
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

}
