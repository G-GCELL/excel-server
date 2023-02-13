package com.gabia.weat.gcellexcelserver.service.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellexcelserver.dto.MessageDto.CreateProgressMsgDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateProgressProducer {

	private final RabbitTemplate rabbitTemplate;

	public void sendMessage(CreateProgressMsgDto createProgressMsgDto) {
		CorrelationData correlationData = new CorrelationData();
		rabbitTemplate.correlationConvertAndSend(createProgressMsgDto, correlationData);
	}

}
