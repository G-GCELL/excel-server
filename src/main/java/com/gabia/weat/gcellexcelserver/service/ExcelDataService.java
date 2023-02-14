package com.gabia.weat.gcellexcelserver.service;

import java.io.File;
import java.sql.SQLException;

import com.gabia.weat.gcellexcelserver.domain.type.MessageType;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.ResultSetDto;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.CreateProgressMsgDto;
import com.gabia.weat.gcellexcelserver.dto.MsgMetaDto;
import com.gabia.weat.gcellexcelserver.file.writer.ExcelWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellexcelserver.repository.ExcelDataJdbcRepository;
import com.gabia.weat.gcellexcelserver.service.producer.CreateProgressProducer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExcelDataService {

	private final ExcelDataJdbcRepository excelDataJdbcRepository;
	private final MinioService minioService;
	private final ExcelWriter excelWriter;
	private final CreateProgressProducer createProgressProducer;

	public void createExcelFile(FileCreateRequestDto dto) throws SQLException {
		ResultSetDto result = excelDataJdbcRepository.getResultSet(dto);
		MsgMetaDto msgMetaDto = new MsgMetaDto(dto.memberId(), dto.fileName());
		File excelFile = excelWriter.writeWithProgress(result, Thread.currentThread().toString(), msgMetaDto);
		minioService.uploadFileWithDelete(excelFile, dto.fileName());
		sendCompletionMsg(msgMetaDto);
	}

	private void sendCompletionMsg(MsgMetaDto dto) {
		createProgressProducer.sendMessage(new CreateProgressMsgDto(
			dto.memberId(),
			MessageType.FILE_CREATION_COMPLETE,
			dto.fileName(),
			null
		));
	}

}
