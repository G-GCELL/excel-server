package com.gabia.weat.gcellexcelserver.service.consumer;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellexcelserver.service.ExcelDataService;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRequestConsumer {

	private final ExcelDataService excelDataService;

	@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
	public void receiveMessage(FileCreateRequestDto fileCreateRequestDto, Channel channel,
		@Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException, SQLException {

		log.info(String.valueOf(fileCreateRequestDto));
		excelDataService.createExcelFile(fileCreateRequestDto);
		channel.basicAck(tag, false);
	}

}
