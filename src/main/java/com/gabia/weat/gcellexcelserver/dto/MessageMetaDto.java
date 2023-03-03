package com.gabia.weat.gcellexcelserver.dto;

import lombok.Builder;

@Builder
public record MessageMetaDto(
	Long memberId,
	Long excelInfoId,
	String fileName,
	String traceId
) {
}
