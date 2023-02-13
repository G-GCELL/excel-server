package com.gabia.weat.gcellexcelserver.repository.query;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;

public interface QueryGenerator {

    String generateQuery(FileCreateRequestDto dto);

    String generateCountQuery(FileCreateRequestDto dto);

}
