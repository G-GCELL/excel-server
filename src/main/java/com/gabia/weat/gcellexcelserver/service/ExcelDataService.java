package com.gabia.weat.gcellexcelserver.service;

import java.io.File;
import java.sql.SQLException;

import com.gabia.weat.gcellexcelserver.domain.type.MessageType;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.ResultSetDto;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.FileCreateProgressMsgDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.dto.MsgMetaDto;
import com.gabia.weat.gcellexcelserver.file.writer.ExcelWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellexcelserver.repository.ExcelDataJdbcRepository;
import com.gabia.weat.gcellexcelserver.service.producer.FileCreateProgressProducer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExcelDataService {

	private final ExcelDataJdbcRepository excelDataJdbcRepository;
	private final MinioService minioService;
	private final ExcelWriter excelWriter;
	private final FileCreateProgressProducer fileCreateProgressProducer;

	public void createExcelFile(MessageWrapperDto<@Valid FileCreateRequestDto> messageWrapperDto) throws SQLException {
		FileCreateRequestDto dto = messageWrapperDto.getMessage();
		ResultSetDto result = excelDataJdbcRepository.getResultSet(dto);
		MsgMetaDto msgMetaDto = new MsgMetaDto(dto.memberId(), dto.fileName());
		File excelFile = excelWriter.writeWithProgress(result, Thread.currentThread().toString(), msgMetaDto);
		minioService.uploadFileWithDelete(excelFile, dto.fileName());
		sendCompletionMsg(msgMetaDto);
	}

	private void sendCompletionMsg(MsgMetaDto dto) {
		fileCreateProgressProducer.sendMessage(new FileCreateProgressMsgDto(
			dto.memberId(),
			MessageType.FILE_CREATION_COMPLETE,
			dto.fileName(),
			null
		));
	}

}
