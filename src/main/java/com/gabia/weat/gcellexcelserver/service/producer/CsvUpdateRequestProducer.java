package com.gabia.weat.gcellexcelserver.service.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellexcelserver.annotation.ProducerLog;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvUpdateRequestProducer implements Producer<CsvUpdateRequestDto> {

	private final RabbitTemplate csvUpdateRequestRabbitTemplate;

	@Override
	@ProducerLog(exchange = "${rabbitmq.exchange.direct-exchange}")
	public void sendMessage(MessageWrapperDto<CsvUpdateRequestDto> message) {
		csvUpdateRequestRabbitTemplate.convertAndSend(message);
	}

}
