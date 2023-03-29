package com.gabia.weat.gcellexcelserver.service.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellcommonmodule.annotation.ProducerLog;
import com.gabia.weat.gcellcommonmodule.dto.MessageDto;
import com.gabia.weat.gcellcommonmodule.dto.MessageDto.FileCreateProgressMsgDto;
import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileCreateProgressProducer implements Producer<FileCreateProgressMsgDto> {

	private final RabbitTemplate fileCreateProgressRabbitTemplate;

	@Override
	@ProducerLog(exchange = "${rabbitmq.exchange.file-create-progress-exchange}")
	public void sendMessage(MessageWrapperDto<FileCreateProgressMsgDto> message) {
		fileCreateProgressRabbitTemplate.convertAndSend(message);
	}

}