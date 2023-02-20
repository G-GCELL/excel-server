package com.gabia.weat.gcellexcelserver.service.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellexcelserver.dto.MessageDto.FileCreateProgressMsgDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileCreateProgressProducer implements Producer<FileCreateProgressMsgDto> {

	private final RabbitTemplate rabbitTemplate;

	public void sendMessage(FileCreateProgressMsgDto fileCreateProgressMsgDto) {
		CorrelationData correlationData = new CorrelationData();
		rabbitTemplate.correlationConvertAndSend(fileCreateProgressMsgDto, correlationData);
	}

}
