package com.gabia.weat.gcellexcelserver.service.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellcommonmodule.annotation.ProducerLog;
import com.gabia.weat.gcellcommonmodule.dto.MessageDto.FileCreateErrorMsgDto;
import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileCreateErrorProducer implements Producer<FileCreateErrorMsgDto> {

	private final RabbitTemplate fileCreateErrorRabbitTemplate;

	@Override
	@ProducerLog(exchange = "${rabbitmq.exchange.file-create-error-exchange}")
	public void sendMessage(MessageWrapperDto<FileCreateErrorMsgDto> message) {
		fileCreateErrorRabbitTemplate.convertAndSend(message);
	}

}