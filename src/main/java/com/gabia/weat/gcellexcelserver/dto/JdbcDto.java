package com.gabia.weat.gcellexcelserver.dto;

import java.sql.ResultSet;
import java.util.Map;

public class JdbcDto {

	public record ResultSetDto(
		ResultSet resultSet,
		Integer resultSetCount
	) {
	}

	public record QuerySetDto(
		String sql,
		Map<Integer, Object> params
	) {
	}

}

