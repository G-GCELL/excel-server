package com.gabia.weat.gcellexcelserver.converter;

import com.gabia.weat.gcellexcelserver.domain.type.MessageType;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.FileCreateProgressMsgDto;
import com.gabia.weat.gcellexcelserver.dto.MessageMetaDto;

public class MessageMetaDtoConverter {

	public static FileCreateProgressMsgDto toFileCreateProgressMsgDto(MessageMetaDto messageMetaDto) {
		return FileCreateProgressMsgDto.builder()
			.memberId(messageMetaDto.memberId())
			.messageType(MessageType.FILE_CREATION_COMPLETE)
			.memberFileName(messageMetaDto.fileName())
			.progressRate(null)
			.build();
	}

	public static FileCreateProgressMsgDto toFileCreateProgressMsgDto(MessageMetaDto messageMetaDto, int rate) {
		return FileCreateProgressMsgDto.builder()
			.memberId(messageMetaDto.memberId())
			.messageType(MessageType.FILE_CREATION_PROGRESS)
			.memberFileName(messageMetaDto.fileName())
			.progressRate(rate)
			.build();
	}

}
