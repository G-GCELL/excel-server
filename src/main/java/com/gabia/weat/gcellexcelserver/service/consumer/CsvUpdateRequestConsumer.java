package com.gabia.weat.gcellexcelserver.service.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellexcelserver.annotation.ConsumerLog;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.service.ExcelDataService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvUpdateRequestConsumer implements Consumer<CsvUpdateRequestDto> {

	private final ExcelDataService excelDataService;

	@Override
	@ConsumerLog(queue = "${rabbitmq.queue.csv-update-request-queue}")
	@RabbitListener(containerFactory = "csvUpdateRequestListenerFactory")
	public void receiveMessage(@Valid MessageWrapperDto<CsvUpdateRequestDto> message) throws Exception {
		CsvUpdateRequestDto dto = message.getMessage();
		excelDataService.updateExcelData(dto.fileLocate(), dto.email(), dto.deleteTarget());
	}

}
