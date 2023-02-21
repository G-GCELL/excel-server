package com.gabia.weat.gcellexcelserver.repository.query;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.QuerySetDto;

public interface QueryGenerator {

    QuerySetDto generateQuery(FileCreateRequestDto dto);

    QuerySetDto generateCountQuery(FileCreateRequestDto dto);

	String generateSingleResultQuery();
}
