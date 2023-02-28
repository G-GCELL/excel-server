package com.gabia.weat.gcellexcelserver.service.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellexcelserver.annotation.ProducerLog;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.FileCreateProgressMsgDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileCreateProgressProducer implements Producer<FileCreateProgressMsgDto> {

	private final RabbitTemplate fileCreateProgressRabbitTemplate;

	@Override
	@ProducerLog(exchange = "${rabbitmq.exchange.file-create-progress-exchange}")
	public void sendMessage(MessageWrapperDto<FileCreateProgressMsgDto> message) {
		CorrelationData correlationData = new CorrelationData();
		fileCreateProgressRabbitTemplate.correlationConvertAndSend(message, correlationData);
	}

}
