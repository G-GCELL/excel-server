package com.gabia.weat.gcellexcelserver.service.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellexcelserver.annotation.ConsumerLog;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.FileCreateRequestMsgDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.service.ExcelDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileCreateRequestConsumer implements Consumer<FileCreateRequestMsgDto> {

	private final ExcelDataService excelDataService;

	@Override
	@ConsumerLog(queue = "${rabbitmq.queue.file-create-request-queue}")
	@RabbitListener(containerFactory = "fileCreateRequestListenerFactory")
	public void receiveMessage(MessageWrapperDto<FileCreateRequestMsgDto> message) throws Exception {
		excelDataService.createExcelFile(message);
	}

}
