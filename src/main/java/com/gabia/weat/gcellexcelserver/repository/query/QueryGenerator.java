package com.gabia.weat.gcellexcelserver.repository.query;

import static com.gabia.weat.gcellcommonmodule.dto.MessageDto.*;

import com.gabia.weat.gcellexcelserver.dto.JdbcDto.QuerySetDto;

public interface QueryGenerator {

    QuerySetDto generateQuery(FileCreateRequestMsgDto dto);

    QuerySetDto generateCountQuery(FileCreateRequestMsgDto dto);

	String generateSingleResultQuery();
}