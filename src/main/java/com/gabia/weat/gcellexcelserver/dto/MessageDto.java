package com.gabia.weat.gcellexcelserver.dto;

import com.gabia.weat.gcellexcelserver.domain.type.MessageType;

import lombok.Builder;

public class MessageDto {

	@Builder
	public record FileCreateProgressMsgDto(
		Long memberId,
		MessageType messageType,
		String memberFileName,
		Integer progressRate
	) {
	}

}
