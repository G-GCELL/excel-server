package com.gabia.weat.gcellexcelserver.dto;

import com.gabia.weat.gcellexcelserver.domain.type.MessageType;

public class MessageDto {

	public record CreateProgressMsgDto(
		Long memberId,
		MessageType messageType,
		String memberFileName,
		Integer progressRate
	) {
	}

}
