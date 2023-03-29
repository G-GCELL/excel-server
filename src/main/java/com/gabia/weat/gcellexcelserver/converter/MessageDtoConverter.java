package com.gabia.weat.gcellexcelserver.converter;

import static com.gabia.weat.gcellcommonmodule.dto.MessageDto.*;

import com.gabia.weat.gcellcommonmodule.dto.MessageDto;
import com.gabia.weat.gcellexcelserver.dto.MessageMetaDto;

public class MessageDtoConverter {

	public static MessageMetaDto toMessageMetaDto(FileCreateRequestMsgDto fileCreateRequestMsgDto, String traceId) {
		return MessageMetaDto.builder()
			.memberId(fileCreateRequestMsgDto.memberId())
			.excelInfoId(fileCreateRequestMsgDto.excelInfoId())
			.fileName(fileCreateRequestMsgDto.fileName())
			.traceId(traceId)
			.build();
	}

	public static FileCreateErrorMsgDto toFileCreateErrorMsgDto(FileCreateRequestMsgDto fileCreateRequestMsgDto,
		String errorMessage, int errorCode) {
		return FileCreateErrorMsgDto.builder()
			.memberId(fileCreateRequestMsgDto.memberId())
			.excelInfoId(fileCreateRequestMsgDto.excelInfoId())
			.errorMessage(errorMessage)
			.errorCode(errorCode)
			.build();
	}

}