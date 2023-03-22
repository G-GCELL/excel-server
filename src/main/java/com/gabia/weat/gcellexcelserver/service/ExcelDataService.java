package com.gabia.weat.gcellexcelserver.service;

import static com.gabia.weat.gcellcommonmodule.dto.MessageDto.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.YearMonth;

import com.gabia.weat.gcellcommonmodule.annotation.TimerLog;
import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.annotation.MailAlarm;
import com.gabia.weat.gcellexcelserver.converter.MessageDtoConverter;
import com.gabia.weat.gcellexcelserver.converter.MessageMetaDtoConverter;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.ResultSetDto;
import com.gabia.weat.gcellexcelserver.dto.MessageMetaDto;
import com.gabia.weat.gcellexcelserver.file.reader.CsvParser;
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
public class ExcelDataService {

	private final CsvParser csvParser;
	private final ExcelWriter excelWriter;
	private final MinioService minioService;
	private final ExcelDataJdbcRepository excelDataJdbcRepository;
	private final FileCreateProgressProducer fileCreateProgressProducer;

	@TimerLog
	@MailAlarm
	public void updateExcelData(String fileLocate, String email, YearMonth deleteTarget)
		throws SQLException, IOException {
		File csvFile = minioService.downloadFile(fileLocate);
		csvParser.insertWithCsv(csvFile, deleteTarget);
	}

	@TimerLog
	@Transactional(readOnly = true)
	public void createExcelFile(@Valid MessageWrapperDto<FileCreateRequestMsgDto> messageWrapperDto) throws SQLException {
		FileCreateRequestMsgDto dto = messageWrapperDto.getMessage();
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