package com.gabia.weat.gcellexcelserver.service.consumer;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.service.ExcelDataService;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileCreateRequestConsumer implements Consumer<FileCreateRequestDto> {

	private final ExcelDataService excelDataService;

	@Override
	@RabbitListener(containerFactory = "fileCreateRequestListenerFactory")
	public void receiveMessage(MessageWrapperDto<FileCreateRequestDto> message, Channel channel,
		@Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException, SQLException {

		log.info(String.valueOf(message));
		getFileCreateRequestMessage(message);
		channel.basicAck(tag, false);
	}

	private void getFileCreateRequestMessage(MessageWrapperDto<FileCreateRequestDto> messageWrapperDto) throws
		SQLException {
		excelDataService.createExcelFile(messageWrapperDto);
	}

}
