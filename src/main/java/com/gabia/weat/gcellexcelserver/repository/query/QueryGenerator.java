package com.gabia.weat.gcellexcelserver.repository.query;

import com.gabia.weat.gcellexcelserver.dto.FileDto;

public interface QueryGenerator {

    String generateQuery(FileDto.FileCreateRequestDto dto);

    String generateCountQuery(FileDto.FileCreateRequestDto dto);

}
