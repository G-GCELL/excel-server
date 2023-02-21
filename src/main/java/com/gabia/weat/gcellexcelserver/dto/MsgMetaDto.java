package com.gabia.weat.gcellexcelserver.dto;

import lombok.Builder;

@Builder
public record MsgMetaDto(
	Long memberId,
	String fileName,
	String traceId
) {
}
