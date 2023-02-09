package com.gabia.weat.gcellexcelserver.dto;

import java.sql.ResultSet;

public record ResultSetDto(
	ResultSet resultSet,
	Integer resultSetCount
) {
}
