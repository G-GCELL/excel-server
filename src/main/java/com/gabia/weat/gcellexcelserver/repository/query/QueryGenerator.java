package com.gabia.weat.gcellexcelserver.repository.query;

import com.gabia.weat.gcellexcelserver.dto.JdbcDto.QuerySetDto;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.FileCreateRequestMsgDto;

public interface QueryGenerator {

    QuerySetDto generateQuery(FileCreateRequestMsgDto dto);

    QuerySetDto generateCountQuery(FileCreateRequestMsgDto dto);

	String generateSingleResultQuery();
}
