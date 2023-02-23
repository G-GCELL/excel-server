package com.gabia.weat.gcellexcelserver.service;

import java.io.File;
import java.sql.SQLException;

import com.gabia.weat.gcellexcelserver.converter.MessageDtoConverter;
import com.gabia.weat.gcellexcelserver.converter.MessageMetaDtoConverter;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.ResultSetDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.dto.MessageMetaDto;
import com.gabia.weat.gcellexcelserver.file.writer.ExcelWriter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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

	public void createExcelFile(@Valid MessageWrapperDto<FileCreateRequestDto> messageWrapperDto) throws SQLException {
		FileCreateRequestDto dto = messageWrapperDto.getMessage();
		ResultSetDto result = excelDataJdbcRepository.getResultSet(dto);
		MessageMetaDto messageMetaDto = MessageDtoConverter.toMessageMetaDto(dto, messageWrapperDto.getTraceId());
		File excelFile = excelWriter.writeWithProgress(result, Thread.currentThread().toString(), messageMetaDto);
		minioService.uploadFileWithDelete(excelFile, dto.fileName());
		sendCompletionMsg(messageMetaDto);
	}

	private void sendCompletionMsg(MessageMetaDto dto) {
		fileCreateProgressProducer.sendMessage(
			MessageWrapperDto.wrapMessageDto(
				MessageMetaDtoConverter.toFileCreateProgressMsgDto(dto), dto.traceId())
		);
	}

}
