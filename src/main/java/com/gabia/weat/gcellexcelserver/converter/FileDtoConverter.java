package com.gabia.weat.gcellexcelserver.converter;

import com.gabia.weat.gcellexcelserver.dto.MessageMetaDto;

public class FileDtoConverter {

	public static MessageMetaDto toMessageMetaDto(FileCreateRequestDto fileCreateRequestDto, String traceId) {
		return MessageMetaDto.builder()
			.memberId(fileCreateRequestDto.memberId())
			.fileName(fileCreateRequestDto.fileName())
			.traceId(traceId)
			.build();
	}

}
